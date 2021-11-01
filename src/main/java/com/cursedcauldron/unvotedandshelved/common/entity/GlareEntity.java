package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.ai.GlareBrain;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.GlowSquid;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

//<>

public class GlareEntity extends AgeableMob implements FlyingAnimal {
    protected static final ImmutableList<SensorType<? extends Sensor<? super GlareEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.BREED_TARGET, UnvotedAndShelved.GRUMPY_TICKS, UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.AVOID_TARGET);
    private static final EntityDataAccessor<Boolean> GRUMPY = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FINDING_DARKNESS = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> GRUMPY_TICKS;
    private static final EntityDataAccessor<Integer> DATA_GLARE_DARK_TICKS_REMAINING;


    public GlareEntity(EntityType<? extends AgeableMob> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new LookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }



    @Override
    protected Brain.Provider<GlareEntity> brainProvider() {
        return Brain.provider(MEMORY_MODULES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return GlareBrain.create(this, this.brainProvider().makeBrain(dynamic));
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GRUMPY, false);
        this.entityData.define(FINDING_DARKNESS, false);
        this.entityData.define(DATA_GLARE_DARK_TICKS_REMAINING, 0);
        this.entityData.define(GRUMPY_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsGrumpy", this.isGrumpy());
        nbt.putInt("GrumpyTicks", this.getGrumpyTick());
        nbt.putInt("FindDarknessTicks", this.getDarkTicksRemaining());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setGrumpy(nbt.getBoolean("IsGrumpy"));
        this.setGrumpyTick(nbt.getInt("GrumpyTicks"));
    }

    @Override
    public boolean isFlying() {
        return this.level.getBlockState(this.blockPosition()).isAir();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public Brain<GlareEntity> getBrain() {
        return (Brain<GlareEntity>)super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("glareBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().pop();
        this.level.getProfiler().push("glareActivityUpdate");
        GlareBrain.updateActivities(this);
        this.level.getProfiler().pop();
        super.customServerAiStep();
        Optional<Integer> optional = this.getBrain().getMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING);
        this.setFindingDarkness(optional.isPresent() && optional.get() > 0);
    }

    public void setGrumpy(boolean isGrumpy) {
        this.entityData.set(GRUMPY, isGrumpy);
    }

    public boolean isGrumpy() {
        return this.entityData.get(GRUMPY);
    }

    public void setFindingDarkness(boolean findingDarkness) {
        this.entityData.set(FINDING_DARKNESS, findingDarkness);
    }

    public static AttributeSupplier.Builder createGlareAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FLYING_SPEED, 0.6000000238418579D).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.MOSS_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.5F, 1.0F);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation navigator = new FlyingPathNavigation(this, world) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        navigator.setCanOpenDoors(false);
        navigator.setCanFloat(false);
        navigator.setCanPassDoors(true);
        return navigator;
    }

    @Override

    public void aiStep() {
        super.aiStep();
        int i = this.getGrumpyTick();
        if (i > 0) {
            this.setGrumpyTick(i - 1);
        }
        GlareEntity entity = this;
        entity.setGrumpy((level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) == 0 && level.getBrightness(LightLayer.SKY, entity.blockPosition()) == 0) || (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) == 0 && level.getDayTime() >= 13000) || (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) == 0 && level.isThundering()));

        int d = this.getDarkTicksRemaining();
        if (d > 0) {
            this.level.addParticle(ParticleTypes.FALLING_SPORE_BLOSSOM, this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
        }
        this.setDarkTicks(d - 1);
    }


    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    protected void jumpInLiquid(Tag<Fluid> fluid) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult actionResult = super.mobInteract(player, hand);
        if (actionResult.consumesAction()) {
            return actionResult;
        } else if (!this.level.isClientSide) {
            return GlareBrain.playerInteract(this, player, hand);
        } else {
            boolean bl = GlareBrain.isGlowBerry(this, player.getItemInHand(hand));
            return bl ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return this.isGrumpy() ? SoundRegistry.GLARE_GRUMPY_IDLE : SoundRegistry.GLARE_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.MOSS_STEP;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.MOSS_BREAK;
    }

    private void setGrumpyTick(int ticks) {
        this.entityData.set(GRUMPY_TICKS, ticks);
    }

    public int getGrumpyTick() {
        return this.entityData.get(GRUMPY_TICKS);
    }

    static {
        GRUMPY_TICKS = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.INT);
    }

    static {
        DATA_GLARE_DARK_TICKS_REMAINING = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.INT);
    }

    public void setDarkTicks(int i) {
        this.entityData.set(DATA_GLARE_DARK_TICKS_REMAINING, i);
    }

    public int getDarkTicksRemaining() {
        return this.entityData.get(DATA_GLARE_DARK_TICKS_REMAINING);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }


}