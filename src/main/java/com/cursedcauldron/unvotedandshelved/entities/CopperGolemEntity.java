package com.cursedcauldron.unvotedandshelved.entities;

import com.cursedcauldron.unvotedandshelved.entities.ai.copper_golem.CopperGolemBrain;
import com.cursedcauldron.unvotedandshelved.init.USEntityTypes;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.init.USPoses;
import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("all")
public class CopperGolemEntity extends AbstractGolem {

    // ඞ

    protected static final ImmutableList<SensorType<? extends Sensor<? super CopperGolemEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get(),  USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS.get(), USMemoryModules.COPPER_BUTTON.get());
    private static final EntityDataAccessor<Integer> STAGE = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.BOOLEAN);

    // Animation States:

    public final AnimationState walkingAnimation = new AnimationState();
    public final AnimationState headSpinAnimation = new AnimationState();
    public final AnimationState headSpinSlowerAnimation = new AnimationState();
    public final AnimationState headSpinSlowestAnimation = new AnimationState();

    public final AnimationState buttonAnimation = new AnimationState();
    public final AnimationState buttonSlowerAnimation = new AnimationState();
    public final AnimationState buttonSlowestAnimation = new AnimationState();

    public final AnimationState buttonUpAnimation = new AnimationState();
    public final AnimationState buttonUpSlowerAnimation = new AnimationState();
    public final AnimationState buttonUpSlowestAnimation = new AnimationState();

    public final AnimationState buttonDownAnimation = new AnimationState();
    public final AnimationState buttonDownSlowerAnimation = new AnimationState();
    public final AnimationState buttonDownSlowestAnimation = new AnimationState();

    public CopperGolemEntity(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
        this.maxUpStep = 1.0F;
    }

    @Override
    protected Brain.Provider<CopperGolemEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return CopperGolemBrain.create(this.brainProvider().makeBrain(dynamic));
    }

    @Override @SuppressWarnings("all")
    public Brain<CopperGolemEntity> getBrain() {
        return (Brain<CopperGolemEntity>)super.getBrain();
    }

    // Sets the eye height of the mob

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * (this.isBaby() ? 0.3f : 0.6f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STAGE, 0);
        this.entityData.define(WAXED, false);
    }

    // Waxed NBT Tag:

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setStage(CopperGolemEntity.Stage.BY_ID[tag.getInt("Stage")]);
        this.setWaxed(tag.getBoolean("Waxed"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Stage", this.getStage().getId());
        tag.putBoolean("Waxed", this.isWaxed());
    }

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("copperGolemBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        this.level.getProfiler().push("copperGolemActivityUpdate");
        CopperGolemBrain.updateActivity(this);
        this.level.getProfiler().pop();
        super.customServerAiStep();
    }

    // Animations:

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (DATA_POSE.equals(data)) {
            this.getPose();
            if (this.getStage() == Stage.UNAFFECTED) {
                if (this.isInPose(USPoses.HEAD_SPIN.get())) {
                    this.headSpinAnimation.start(this.tickCount);
                } else {
                    this.headSpinAnimation.stop();
                    this.headSpinSlowerAnimation.stop();
                    this.headSpinSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON.get())) {
                    this.buttonAnimation.start(this.tickCount);
                } else {
                    this.buttonAnimation.stop();
                    this.buttonSlowerAnimation.stop();
                    this.buttonSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON_UP.get())) {
                    this.buttonUpAnimation.start(this.tickCount);
                } else {
                    this.buttonUpAnimation.stop();
                    this.buttonUpSlowerAnimation.stop();
                    this.buttonUpSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON_DOWN.get())) {
                    this.buttonDownAnimation.start(this.tickCount);
                } else {
                    this.buttonDownAnimation.stop();
                    this.buttonDownSlowerAnimation.stop();
                    this.buttonDownSlowestAnimation.stop();
                }
            } else if (this.getStage() == Stage.EXPOSED) {
                if (this.isInPose(USPoses.HEAD_SPIN.get())) {
                    this.headSpinSlowerAnimation.start(this.tickCount);
                } else {
                    this.headSpinAnimation.stop();
                    this.headSpinSlowerAnimation.stop();
                    this.headSpinSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON.get())) {
                    this.buttonSlowerAnimation.start(this.tickCount);
                } else {
                    this.buttonAnimation.stop();
                    this.buttonSlowerAnimation.stop();
                    this.buttonSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON_UP.get())) {
                    this.buttonUpSlowerAnimation.start(this.tickCount);
                } else {
                    this.buttonUpAnimation.stop();
                    this.buttonUpSlowerAnimation.stop();
                    this.buttonUpSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON_DOWN.get())) {
                    this.buttonDownSlowerAnimation.start(this.tickCount);
                } else {
                    this.buttonDownAnimation.stop();
                    this.buttonDownSlowerAnimation.stop();
                    this.buttonDownSlowestAnimation.stop();
                }
            } else {
                if (this.isInPose(USPoses.HEAD_SPIN.get())) {
                    this.headSpinSlowestAnimation.start(this.tickCount);
                } else {
                    this.headSpinAnimation.stop();
                    this.headSpinSlowerAnimation.stop();
                    this.headSpinSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON.get())) {
                    this.buttonSlowestAnimation.start(this.tickCount);
                } else {
                    this.buttonAnimation.stop();
                    this.buttonSlowerAnimation.stop();
                    this.buttonSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON_UP.get())) {
                    this.buttonUpSlowestAnimation.start(this.tickCount);
                } else {
                    this.buttonUpAnimation.stop();
                    this.buttonUpSlowerAnimation.stop();
                    this.buttonUpSlowestAnimation.stop();
                }
                if (this.isInPose(USPoses.PRESS_BUTTON_DOWN.get())) {
                    this.buttonDownSlowestAnimation.start(this.tickCount);
                } else {
                    this.buttonDownAnimation.stop();
                    this.buttonDownSlowerAnimation.stop();
                    this.buttonDownSlowestAnimation.stop();
                }
            }
        }
        super.onSyncedDataUpdated(data);
    }

    private boolean isInPose(Pose pose) {
        return this.getPose() == pose;
    }

    public CopperGolemEntity.Stage getStage() {
        return CopperGolemEntity.Stage.BY_ID[this.entityData.get(STAGE)];
    }

    // Changes the movement speed depending on how much the Copper Golem has oxidized

    @SuppressWarnings("all")
    public void setStage(CopperGolemEntity.Stage stage) {
        this.entityData.set(STAGE, stage.getId());

        switch (stage) {
            case UNAFFECTED -> this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5D);
            case EXPOSED -> this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.375D);
            case WEATHERED -> this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        }
    }

    public boolean isWaxed() {
        return this.entityData.get(WAXED);
    }

    public void setWaxed(boolean waxed) {
        this.entityData.set(WAXED, waxed);
    }

    // Entity Attributes

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    // Plays Walking Animation if Entity is Moving

    private boolean shouldWalk() {
        return this.onGround && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6 && !this.isInWaterOrBubble();
    }

    // Prevents the Copper Golem from drowning, similar to Iron Golems

    protected int decreaseAirSupply(int i) {
        return i;
    }

    @Override
    public void tick() {
        if (this.level.isClientSide()) {
            if (this.shouldWalk()) {
                this.walkingAnimation.startIfStopped(this.tickCount);

            } else {
                this.walkingAnimation.stop();
            }
        }
        super.tick();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {

        // Copper Golem waxing using Honeycomb

        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.HONEYCOMB) && !this.isWaxed()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            this.setWaxed(true);
            this.level.levelEvent(player, 3003, this.blockPosition(), 0);
            this.gameEvent(GameEvent.ENTITY_INTERACT, this);
            return InteractionResult.SUCCESS;
        }

        // Copper Golem scraping using an Axe

        else if (stack.getItem() instanceof AxeItem) {
            if (this.isWaxed()) {
                this.setWaxed(false);
                this.level.playSound(player, this.blockPosition(), SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.level.levelEvent(player, 3004, this.blockPosition(), 0);
                this.gameEvent(GameEvent.ENTITY_INTERACT, this);
            } else {
                if (this.getStage() != Stage.UNAFFECTED) {
                    stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(hand));
                    this.setStage(Stage.values()[this.getStage().getId() - 1]);
                    this.level.playSound(player, this.blockPosition(), SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.level.levelEvent(player, 3005, this.blockPosition(), 0);
                    this.gameEvent(GameEvent.ENTITY_INTERACT, this);
                } else {
                    return InteractionResult.PASS;
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Copper Golem repairing using a Copper Ingot

        else if (this.getHealth() < this.getMaxHealth() && stack.is(Items.COPPER_INGOT)) {
            this.heal(5.0F);
            float f1 = 1.4F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
            this.playSound(USSoundEvents.COPPER_GOLEM_REPAIR.get(), 0.5F, f1);
            this.gameEvent(GameEvent.ENTITY_INTERACT, this);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    // Converts the Copper Golem to an Oxidized Copper Golem if fully oxidized

    public <T extends Mob> void convertToFrozen(EntityType<T> entityType, boolean bl) {
        if (!this.isRemoved()) {
            FrozenCopperGolemEntity mob = (FrozenCopperGolemEntity)entityType.create(this.level);
            assert mob != null;
            mob.copyPosition(this);
            mob.lookAt(EntityAnchorArgument.Anchor.EYES, this.getLookAngle());
            mob.setYBodyRot(this.getYRot());
            mob.setYHeadRot(this.getYHeadRot());
            if (this.hasCustomName()) {
                mob.setCustomName(this.getCustomName());
            }
            if (this.isPersistenceRequired()) {
                mob.setPersistenceRequired();
            }
            mob.setInvulnerable(this.isInvulnerable());

            if (bl) {
                mob.setCanPickUpLoot(this.canPickUpLoot());
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    ItemStack itemStack = this.getItemBySlot(equipmentSlot);
                    if (itemStack.isEmpty()) continue;
                    mob.setItemSlot(equipmentSlot, itemStack.copy());
                    mob.setDropChance(equipmentSlot, this.getEquipmentDropChance(equipmentSlot));
                    itemStack.setCount(0);
                }
            }

            this.level.addFreshEntity(mob);
            if (this.isPassenger()) {
                Entity entity = this.getVehicle();
                this.stopRiding();
                if (entity != null) mob.startRiding(entity, true);
            }
            this.discard();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide()) {
            float chance = this.random.nextFloat();
            if (chance < 3.4290552E-11F) {
                this.playSound(USSoundEvents.CHINESE_RIP_OFF_WINNIE_THE_POOH.get(), 1.0F, 1.0F);
            }
            if (this.getStage() == Stage.OXIDIZED) {
                this.convertToFrozen(USEntityTypes.FROZEN_COPPER_GOLEM.get(),true);
            }

            // Allows the Copper Golem to oxidize over time if not waxed

            if (!this.isWaxed() && this.getStage() != Stage.OXIDIZED) {
                float randomChance = this.random.nextFloat();
                if (randomChance < 3.4290552E-5F && this.getStage() != Stage.OXIDIZED) {
                    this.setStage(Stage.values()[this.getStage().getId() + 1]);
                }
            }

        }
    }

    // Cooldown for the Copper Golem pressing Copper Buttons



    // Sound Events

    protected SoundEvent getHurtSound(DamageSource source) {
        return USSoundEvents.COPPER_GOLEM_HIT.get();
    }

    protected SoundEvent getDeathSound() {
        return USSoundEvents.COPPER_GOLEM_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return USSoundEvents.COPPER_GOLEM_WALK.get();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.5F, 1.0F);
    }

    // Sounds depending on what stage of oxidization the Copper Golem is on

    public enum Stage {
        UNAFFECTED(0, "unaffected", USSoundEvents.HEAD_SPIN.get()),
        EXPOSED(1, "exposed", USSoundEvents.HEAD_SPIN_SLOWER.get()),
        WEATHERED(2, "weathered", USSoundEvents.HEAD_SPIN_SLOWEST.get()),
        OXIDIZED(3, "oxidized", null);

        public static final CopperGolemEntity.Stage[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(CopperGolemEntity.Stage::getId)).toArray(Stage[]::new);
        private final int id;
        private final String name;
        private final SoundEvent soundEvent;

        Stage(int id, String name, SoundEvent soundEvent) {
            this.id = id;
            this.name = name;
            this.soundEvent = soundEvent;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }
    }
}