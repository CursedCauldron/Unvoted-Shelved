package com.cursedcauldron.unvotedandshelved.entities;

import com.cursedcauldron.unvotedandshelved.entities.ai.copper_golem.CopperGolemBrain;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class CopperGolemEntity extends AbstractGolem implements PowerableMob {
    protected static final ImmutableList<SensorType<? extends Sensor<? super CopperGolemEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get(), USMemoryModules.COPPER_BUTTON.get());
    private static final EntityDataAccessor<Integer> STAGE = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.BOOLEAN);

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
        this.entityData.define(WAXED, false);
        this.entityData.define(POWERED, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setStage(CopperGolemEntity.Stage.BY_ID[tag.getInt("Stage")]);
        this.setWaxed(tag.getBoolean("Waxed"));
        this.entityData.set(POWERED, tag.getBoolean("powered"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Stage", this.getStage().getId());
        tag.putBoolean("Waxed", this.isWaxed());
        if (this.entityData.get(POWERED)) {
            tag.putBoolean("powered", true);
        }
    }

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("copperbuttonBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        this.level.getProfiler().push("copperbuttonActivityUpdate");
        CopperGolemBrain.updateActivity(this);
        this.level.getProfiler().pop();
        super.customServerAiStep();
    }

    public CopperGolemEntity.Stage getStage() {
        return CopperGolemEntity.Stage.BY_ID[this.entityData.get(STAGE)];
    }

    private void setStage(CopperGolemEntity.Stage stage) {
        this.entityData.set(STAGE, stage.getId());
    }

    public boolean isWaxed() {
        return this.entityData.get(WAXED);
    }

    public void setWaxed(boolean waxed) {
        this.entityData.set(WAXED, waxed);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.HONEYCOMB) && !this.isWaxed()) {
            this.setWaxed(true);
            this.level.levelEvent(player, 3003, this.blockPosition(), 0);
            return InteractionResult.SUCCESS;
        }
        else if (stack.getItem() instanceof AxeItem && this.isWaxed()) {
            this.setWaxed(false);
            this.level.playSound(player, this.blockPosition(), SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.level.levelEvent(player, 3004, this.blockPosition(), 0);
            return InteractionResult.SUCCESS;
        }
        else if (this.getHealth() < this.getMaxHealth() && stack.is(Items.COPPER_INGOT)) {
            this.heal(5.0F);
            float f1 = 1.4F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
            this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 0.5F, f1);
            this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
//        System.out.println(this.getBrain().getRunningBehaviors());
//        System.out.println(this.getBrain().getActiveActivities());
        Optional<Integer> memory = this.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get());
        memory.ifPresent(integer -> System.out.println("The copper button cooldown is " + integer));
    }

    @Override
    public boolean isPowered() {
        return this.entityData.get(POWERED);
    }

    @Override
    public void thunderHit(ServerLevel p_19927_, LightningBolt p_19928_) {
        super.thunderHit(p_19927_, p_19928_);
        if (this.getStage() == Stage.UNAFFECTED) {
            this.entityData.set(POWERED, true);
        }
    }

    public enum Stage {
        UNAFFECTED(0, "unaffected"),
        EXPOSED(1, "exposed"),
        WEATHERED(2, "weathered"),
        OXIDIZED(3, "oxidized");

        public static final CopperGolemEntity.Stage[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(CopperGolemEntity.Stage::getId)).toArray(Stage[]::new);
        private final int id;
        private final String name;

        Stage(int id, String name) {
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
