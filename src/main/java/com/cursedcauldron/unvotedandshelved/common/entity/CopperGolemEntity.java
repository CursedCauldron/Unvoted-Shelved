package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.ai.CopperGolemBrain;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;

//<>
public class CopperGolemEntity extends GolemEntity implements IAnimatable, IAnimationTickable {
    protected static final ImmutableList<SensorType<? extends Sensor<? super CopperGolemEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(UnvotedAndShelved.GLOWBERRIES_GIVEN, UnvotedAndShelved.GRUMPY_TICKS, UnvotedAndShelved.DARK_TICKS_REMAINING, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.AVOID_TARGET);
    private static final TrackedData<Integer> STAGES = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SPEED = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> WAXED = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> BUTTON_TICKS = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> BUTTON_TICKS_DOWN = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> BUTTON_TICKS_UP = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private int cooldownTicks;
    @Nullable
    private UUID lightningId;
    private AnimationFactory factory = new AnimationFactory(this);
    private long lastHitTime;

    public CopperGolemEntity(EntityType<? extends GolemEntity> entityType, World level) {
        super(entityType, level);
        this.ignoreCameraFrustum = true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STAGES, 0);
        this.dataTracker.startTracking(BUTTON_TICKS, 0);
        this.dataTracker.startTracking(BUTTON_TICKS_DOWN, 0);
        this.dataTracker.startTracking(BUTTON_TICKS_UP, 0);
        this.dataTracker.startTracking(SPEED, 120);
        this.dataTracker.startTracking(WAXED, false);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getButtonTicks() > 0 || getButtonDownTicks() > 0 || getButtonUpTicks() > 0) {
            if (getButtonTicks() > 0) {
                event.getController().setAnimation(new AnimationBuilder().clearAnimations());
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coppergolem.button", false));
            }
            if (getButtonDownTicks() > 0) {
                event.getController().setAnimation(new AnimationBuilder().clearAnimations());
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coppergolem.button_down", false));
            }
            if (getButtonUpTicks() > 0) {
                event.getController().setAnimation(new AnimationBuilder().clearAnimations());
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coppergolem.button_up", false));
            }
        } else {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coppergolem.walk", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coppergolem.idle", true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, this::predicate));
    }

    @Override
    public boolean canMoveVoluntarily() {
        return super.canMoveVoluntarily() && this.getOxidationStage() != OxidationStage.OXIDIZED;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static DefaultAttributeContainer.Builder createGolemAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2600000238418579D);
    }

    @Override
    protected void mobTick() {
        this.world.getProfiler().push("coppergolemBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().pop();
        this.world.getProfiler().push("coppergolemActivityUpdate");
        CopperGolemBrain.updateActivities(this);
        this.world.getProfiler().pop();
        int i = this.getCooldownTicks();
        if (i > 0) {
            this.setCooldownTicks(i - 1);
        }
        int b = this.getButtonTicks();
        if (b > 0) {
            this.getNavigation().stop();
            this.setButtonTicks(b - 1);
        }
        int c = this.getButtonDownTicks();
        if (c > 0) {
            this.getNavigation().stop();
            this.setButtonDownTicks(c - 1);
        }
        int d = this.getButtonUpTicks();
        if (d > 0) {
            this.getNavigation().stop();
            this.setButtonUpTicks(d - 1);
        }
        System.out.println("b = " + (b));
    }

    @Override
    protected Brain.Profile<CopperGolemEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return CopperGolemBrain.create(this, this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setCooldownTicks(nbt.getInt("CooldownTicks"));
        this.setOxidationStage(OxidationStage.OXIDATION_STAGES[nbt.getInt("OxidationStage")]);
        this.setWaxed(nbt.getBoolean("Waxed"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("CooldownTicks", this.getCooldownTicks());
        nbt.putInt("OxidationStage", this.getOxidationStage().getId());
        nbt.putBoolean("Waxed", this.isWaxed());
    }

    public void setWaxed(boolean waxed) {
        this.dataTracker.set(WAXED, waxed);
    }

    public boolean isWaxed() {
        return this.dataTracker.get(WAXED);
    }

    public void setOxidationStage(OxidationStage stage) {
        this.dataTracker.set(STAGES, stage.getId());
    }

    public OxidationStage getOxidationStage() {
        return OxidationStage.OXIDATION_STAGES[this.dataTracker.get(STAGES)];
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public void setButtonTicks(int ticks) {
        this.dataTracker.set(BUTTON_TICKS, ticks);
    }

    public int getButtonTicks() {
        return this.dataTracker.get(BUTTON_TICKS);
    }

    public void setButtonDownTicks(int ticks) {
        this.dataTracker.set(BUTTON_TICKS_DOWN, ticks);
    }

    public int getButtonDownTicks() {
        return this.dataTracker.get(BUTTON_TICKS_DOWN);
    }

    public void setButtonUpTicks(int ticks) {
        this.dataTracker.set(BUTTON_TICKS_UP, ticks);
    }

    public int getButtonUpTicks() {
        return this.dataTracker.get(BUTTON_TICKS_UP);
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        UUID uuid = lightning.getUuid();
        OxidationStage stage = getRandomStage(this.random);
        if (!uuid.equals(this.lightningId)) {
            this.lightningId = uuid;
            this.setOxidationStage(stage);
            world.syncWorldEvent(3002, this.getBlockPos(), 0);
        }
    }

    public static OxidationStage getRandomStage(Random random) {
        return random.nextBoolean() && random.nextInt(15) == 0 ? OxidationStage.EXPOSED : OxidationStage.WEATHERED;
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
        else if (stack.getItem() instanceof AxeItem) {
            if (this.isWaxed()) {
                this.setWaxed(false);
                this.world.playSound(player, this.getBlockPos(), SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.world.syncWorldEvent(player, 3004, this.getBlockPos(), 0);
                return ActionResult.SUCCESS;
            } else if (this.getOxidationStage().getId() > 0) {
                this.setOxidationStage(OxidationStage.OXIDATION_STAGES[this.getOxidationStage().getId() - 1]);
                this.world.playSound(player, this.getBlockPos(), SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.world.syncWorldEvent(player, 3005, this.getBlockPos(), 0);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        }
        else if (stack.isOf(Items.WATER_BUCKET) && this.getOxidationStage().getId() < 3) {
            this.setOxidationStage(OxidationStage.OXIDATION_STAGES[this.getOxidationStage().getId() + 1]);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public Brain<CopperGolemEntity> getBrain() {
        return (Brain<CopperGolemEntity>)super.getBrain();
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
    public int tickTimer() {
        return age;
    }

    public int getCooldownState() {
        return getDataTracker().get(SPEED);
    }

    private void playBreakSound() {
        this.world.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_COPPER_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
    }

    private void updateHealth(DamageSource damageSource, float amount) {
        float f = this.getHealth();
        f -= amount;
        if (f <= 0.5F) {
            this.playBreakSound();
            this.drop(damageSource);
            this.kill();
        } else {
            this.setHealth(f);
            this.emitGameEvent(GameEvent.ENTITY_DAMAGED, damageSource.getAttacker());
        }
    }

    @Override
    public void kill() {
        if (this.getOxidationStage() == OxidationStage.OXIDIZED) {
            this.remove(RemovalReason.KILLED);
        } else {
            super.kill();
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.world.isClient && !this.isRemoved() && this.getOxidationStage() == OxidationStage.OXIDIZED) {
            if (DamageSource.OUT_OF_WORLD.equals(source)) {
                this.kill();
                return false;
            } else if (!this.isInvulnerableTo(source)) {
                if (source.isExplosive()) {
                    this.playBreakSound();
                    this.drop(source);
                    this.kill();
                    return false;
                } else if (DamageSource.IN_FIRE.equals(source)) {
                    if (this.isOnFire()) {
                        this.updateHealth(source, 0.15F);
                    } else {
                        this.setOnFireFor(5);
                    }

                    return false;
                } else if (DamageSource.ON_FIRE.equals(source) && this.getHealth() > 0.5F) {
                    this.updateHealth(source, 4.0F);
                    return false;
                } else {
                    boolean bl = source.getSource() instanceof PersistentProjectileEntity;
                    boolean bl2 = bl && ((PersistentProjectileEntity)source.getSource()).getPierceLevel() > 0;
                    boolean bl3 = "player".equals(source.getName());
                    if (!bl3 && !bl) {
                        return false;
                    } else if (source.getAttacker() instanceof PlayerEntity && !((PlayerEntity)source.getAttacker()).getAbilities().allowModifyWorld) {
                        return false;
                    } else if (source.isSourceCreativePlayer()) {
                        this.playBreakSound();
                        this.spawnBreakParticles();
                        this.kill();
                        return bl2;
                    } else {
                        long l = this.world.getTime();
                        if (l - this.lastHitTime > 5L && !bl) {
                            this.world.sendEntityStatus(this, (byte)32);
                            this.emitGameEvent(GameEvent.ENTITY_DAMAGED, source.getAttacker());
                            this.lastHitTime = l;
                        } else {
                            this.playBreakSound();
                            this.drop(source);
                            this.spawnBreakParticles();
                            this.kill();
                        }

                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void spawnBreakParticles() {
        if (this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.COPPER_BLOCK.getDefaultState()), this.getX(), this.getBodyY(0.6666666666666666D), this.getZ(), 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05D);
        }
    }

    public enum OxidationStage {
        UNAFFECTED(0, "unaffected"),
        EXPOSED(1, "exposed"),
        WEATHERED(2, "weathered"),
        OXIDIZED(3, "oxidized");

        public static final CopperGolemEntity.OxidationStage[] OXIDATION_STAGES = Arrays.stream(values()).sorted(Comparator.comparingInt(OxidationStage::getId)).toArray(OxidationStage[]::new);
        private final int id;
        private final String name;

        OxidationStage(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }
}