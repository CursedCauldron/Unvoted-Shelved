package com.cursedcauldron.unvotedandshelved.entities;

import com.cursedcauldron.unvotedandshelved.entities.ai.copper_golem.CopperGolemBrain;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Comparator;

public class CopperGolemEntity extends AbstractGolem {
    protected static final ImmutableList<SensorType<? extends Sensor<? super CopperGolemEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.PLAY_DEAD_TICKS, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN);
    private static final EntityDataAccessor<Integer> STAGE = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);

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

    @Override
    public Brain<CopperGolemEntity> getBrain() {
        return (Brain<CopperGolemEntity>) super.getBrain();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STAGE, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariant(CopperGolemEntity.Stage.BY_ID[tag.getInt("Stage")]);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Stage", this.getVariant().getId());
    }

    public CopperGolemEntity.Stage getVariant() {
        return CopperGolemEntity.Stage.BY_ID[this.entityData.get(STAGE)];
    }

    private void setVariant(CopperGolemEntity.Stage stage) {
        this.entityData.set(STAGE, stage.getId());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
