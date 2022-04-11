package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationState;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.CopperGolemBrain;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.Level;

//<>

public class CopperGolemEntity extends AbstractGolem {
    protected static final ImmutableList<SensorType<? extends Sensor<? super CopperGolemEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.PLAY_DEAD_TICKS, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN);
    private static final EntityDataAccessor<Integer> STAGE = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BUTTON_TICKS = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SPEED = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BUTTON_TICKS_DOWN = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BUTTON_TICKS_UP = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COOLDOWN_TICKS = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState walkingAnimation = new AnimationState();
    public final AnimationState headSpinAnimation = new AnimationState();

    public CopperGolemEntity(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
    }

    @Override
    protected Brain.Provider<CopperGolemEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return CopperGolemBrain.create(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override @SuppressWarnings("all")
    public Brain<CopperGolemEntity> getBrain() {
        return (Brain<CopperGolemEntity>) super.getBrain();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COOLDOWN_TICKS, 0);
        this.entityData.define(STAGE, 0);
        this.entityData.define(BUTTON_TICKS, 0);
        this.entityData.define(SPEED, 120);
        this.entityData.define(BUTTON_TICKS_DOWN, 0);
        this.entityData.define(BUTTON_TICKS_UP, 0);
        this.entityData.define(WAXED, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setCooldownTicks(tag.getInt("CooldownTicks"));
        this.setWaxed(tag.getBoolean("Waxed"));
        this.setOxidationStage(CopperGolemEntity.Stage.BY_ID[tag.getInt("OxidationStage")]);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CooldownTicks", this.getCooldownTicks());
        tag.putInt("OxidationStage", this.getOxidationStage().getId());
        tag.putBoolean("Waxed", this.isWaxed());
    }


    @Override
    public void customServerAiStep() {
        this.level.getProfiler().push("coppergolemBrain");
        this.getBrain().tick((ServerLevel) this.level, this);
        this.level.getProfiler().pop();
        this.level.getProfiler().push("coppergolemActivityUpdate");
        CopperGolemBrain.updateActivities(this);
        this.level.getProfiler().pop();
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
        System.out.println("i = " + (i));
    }

    private boolean shouldWalk() {
        return this.onGround && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6 && !this.isInWaterOrBubble();
    }

    @Override
    public void tick() {
        if (this.level.isClientSide()) {
            if (this.shouldWalk()) {
                this.walkingAnimation.startIfNotRunning();
            } else {
                this.walkingAnimation.stop();
            }
        }
        super.tick();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (DATA_POSE.equals(data)) {
            Pose pose = this.getPose();
            if (pose == EntityPoses.HEAD_SPIN) {
                this.headSpinAnimation.start();
            } else {
                this.headSpinAnimation.stop();
            }
        }
        super.onSyncedDataUpdated(data);
    }



    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public void setWaxed(boolean waxed) {
        this.entityData.set(WAXED, waxed);
    }

    public boolean isWaxed() {
        return this.entityData.get(WAXED);
    }

    public void setOxidationStage(Stage stage) {
        this.entityData.set(STAGE, stage.getId());
    }

    public Stage getOxidationStage() {
        return Stage.BY_ID[this.entityData.get(STAGE)];
    }

    public int getCooldownTicks() {
        return this.entityData.get(COOLDOWN_TICKS);
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.entityData.set(COOLDOWN_TICKS, cooldownTicks);
    }

    public void setButtonTicks(int ticks) {
        this.entityData.set(BUTTON_TICKS, ticks);
    }
    public int getButtonTicks() {
        return this.entityData.get(BUTTON_TICKS);
    }
    public void setButtonDownTicks(int ticks) {
        this.entityData.set(BUTTON_TICKS_DOWN, ticks);
    }
    public int getButtonDownTicks() {
        return this.entityData.get(BUTTON_TICKS_DOWN);
    }
    public void setButtonUpTicks(int ticks) {
        this.entityData.set(BUTTON_TICKS_UP, ticks);
    }
    public int getButtonUpTicks() {
        return this.entityData.get(BUTTON_TICKS_UP);
    }

    public int getCooldownState() {
        return getEntityData().get(SPEED);
    }


    public enum Stage {
        UNAFFECTED(0, "unaffected"),
        EXPOSED(1, "exposed"),
        WEATHERED(2, "weathered"),
        OXIDIZED(3, "oxidized");

        public static final CopperGolemEntity.Stage[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(CopperGolemEntity.Stage::getId)).toArray(Stage[]::new);
        private final int id;
        private final String name;

        Stage(int p_149239_, String p_149240_) {
            this.id = p_149239_;
            this.name = p_149240_;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }
}