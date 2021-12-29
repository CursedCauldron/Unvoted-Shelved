package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.ai.goals.FindButtonGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

//<>

public class CopperGolemEntity extends GolemEntity implements IAnimatable, IAnimationTickable {
    private static final TrackedData<Integer> STAGES = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> WAXED = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int cooldownTicks;
    private boolean canPerformGoals;
    @Nullable
    private UUID lightningId;
    AnimationFactory factory = new AnimationFactory(this);

    public CopperGolemEntity(EntityType<? extends GolemEntity> entityType, World level) {
        super(entityType, level);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STAGES, 0);
        this.dataTracker.startTracking(WAXED, false);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("coppergolem.animation.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("coppergolem.animation.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<CopperGolemEntity>(this, "controller", 1, this::predicate));
    }

    @Override
    public boolean canMoveVoluntarily() {
        return super.canMoveVoluntarily() && this.getOxidationStage() < 3;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static DefaultAttributeContainer.Builder createGolemAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2600000238418579D);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setCooldownTicks(nbt.getInt("CooldownTicks"));
        this.setOxidationStage(nbt.getInt("OxidationStage"));
        this.setPerformGoal(nbt.getBoolean("PerformGoal"));
        this.setWaxed(nbt.getBoolean("Waxed"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("CooldownTicks", this.getCooldownTicks());
        nbt.putInt("OxidationStage", this.getOxidationStage());
        nbt.putBoolean("PerformGoal", this.canPerformGoal());
        nbt.putBoolean("Waxed", this.isWaxed());
    }

    public void setWaxed(boolean waxed) {
        this.dataTracker.set(WAXED, waxed);
    }

    public boolean isWaxed() {
        return this.dataTracker.get(WAXED);
    }

    public void setOxidationStage(int oxidationStage) {
        this.dataTracker.set(STAGES, oxidationStage);
    }

    public int getOxidationStage() {
        return this.dataTracker.get(STAGES);
    }

    public void setPerformGoal(boolean performGoal) {
        this.canPerformGoals = performGoal;
    }

    public boolean canPerformGoal() {
        return this.canPerformGoals;
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        UUID uuid = lightning.getUuid();
        if (!uuid.equals(this.lightningId)) {
            this.lightningId = uuid;
            this.setOxidationStage(this.getOxidationStage() - 1);
            world.syncWorldEvent(3002, this.getBlockPos(), 0);
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.HONEYCOMB) && !this.isWaxed()) {
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            this.world.syncWorldEvent(player, 3003, this.getBlockPos(), 0);
            this.setWaxed(true);
            return ActionResult.SUCCESS;
        }
        else if (stack.getItem() instanceof AxeItem && this.getOxidationStage() > 0) {
            if (this.isWaxed()) {
                this.setWaxed(false);
                this.world.playSound(player, this.getBlockPos(), SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.world.syncWorldEvent(player, 3004, this.getBlockPos(), 0);
            } else {
                this.setOxidationStage(this.getOxidationStage() - 1);
                world.playSound(player, this.getBlockPos(), SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.syncWorldEvent(player, 3005, this.getBlockPos(), 0);
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (!this.world.isClient()) {
            if (!this.canPerformGoal()) {
                this.setCooldownTicks(this.getCooldownTicks() - 1);
            }
        }
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.BLOCK_COPPER_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.5F, 1.25F);
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_COPPER_HIT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_COPPER_BREAK;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new FindButtonGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0F));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(4, new LookAtEntityGoal(this, VillagerEntity.class, 6.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    @Override
    public int tickTimer() {
        return age;
    }
}