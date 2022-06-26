package com.cursedcauldron.unvotedandshelved.entities;

import com.cursedcauldron.unvotedandshelved.entities.ai.glare.GlareBrain;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.init.USParticleTypes;
import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class GlareEntity extends AgeableMob implements FlyingAnimal {
    protected static final ImmutableList<SensorType<? extends Sensor<? super GlareEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(USMemoryModules.GLOWBERRIES_GIVEN.get(), USMemoryModules.GRUMPY_TICKS.get(), USMemoryModules.DARK_TICKS_REMAINING.get(), MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.AVOID_TARGET);
    private static final EntityDataAccessor<Boolean> GRUMPY = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHINY = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FINDING_DARKNESS = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> GRUMPY_TICKS = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> GLOWBERRIES_GIVEN = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.INT);

    public GlareEntity(EntityType<? extends AgeableMob> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 5, true);
        this.lookControl = new LookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    // Glare Spawning

    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor serverLevelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMobGroupData(false);
        }

        this.setShiny(this.getRandom().nextInt(100) == 1);

        return spawnGroupData;
    }

    @Override
    protected Brain.Provider<GlareEntity> brainProvider() {
        return Brain.provider(MEMORY_MODULES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return GlareBrain.create(this.brainProvider().makeBrain(dynamic));
    }

    // NBT Data

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GRUMPY, false);
        this.entityData.define(SHINY, false);
        this.entityData.define(FINDING_DARKNESS, false);
        this.entityData.define(GRUMPY_TICKS, 0);
        this.entityData.define(GLOWBERRIES_GIVEN, 0);
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.getBrain().hasMemoryValue(USMemoryModules.GIVEN_GLOWBERRY.get());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsGrumpy", this.isGrumpy());
        nbt.putBoolean("IsShiny", this.isShiny());
        nbt.putInt("GrumpyTicks", this.getGrumpyTick());
        if (this.brain.getMemory(USMemoryModules.DARK_TICKS_REMAINING.get()).isPresent()) {
            nbt.putInt("FindDarknessTicks", this.brain.getMemory(USMemoryModules.DARK_TICKS_REMAINING.get()).get());
        }
        if (this.brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).isPresent()) {
            nbt.putInt("GlowberriesGiven", this.brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setGrumpy(nbt.getBoolean("IsGrumpy"));
        this.setShiny(nbt.getBoolean("IsShiny"));
        this.setGrumpyTick(nbt.getInt("GrumpyTicks"));
        this.setGlowberries(nbt.getInt("GlowberriesGiven"));
    }

    @Override
    public boolean isFlying() {
        return this.level.getBlockState(this.blockPosition()).isAir();
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override @SuppressWarnings("all")
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
        if (!this.isNoAi()) {
            Optional<Integer> ticksRemaining = this.getBrain().getMemory(USMemoryModules.DARK_TICKS_REMAINING.get());
            this.setFindingDarkness(ticksRemaining.isPresent() && ticksRemaining.get() > 0);
        }
    }

    public void setGrumpy(boolean isGrumpy) {
        this.entityData.set(GRUMPY, isGrumpy);
    }


    public void setShiny(boolean isShiny) {
        this.entityData.set(SHINY, isShiny);
    }

    public boolean isGrumpy() {
        return this.entityData.get(GRUMPY);
    }

    public boolean isShiny() {
        return this.entityData.get(SHINY);
    }

    public void setFindingDarkness(boolean findingDarkness) {
        this.entityData.set(FINDING_DARKNESS, findingDarkness);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.FLYING_SPEED, 0.6000000238418579D).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    // Sends Debug Packets to Server:

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected PathNavigation createNavigation(@NotNull Level world) {
        FlyingPathNavigation navigator = new FlyingPathNavigation(this, world) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        navigator.setCanOpenDoors(false);
        navigator.setCanFloat(true);
        navigator.setCanPassDoors(true);
        return navigator;
    }

    // Makes the Glare become grumpy when in light levels of 0

    private void updateGrumpy(Level level) {
        if (!level.isClientSide()) {
            int skyLight = this.level.getSkyDarken();
            if (skyLight > 0) {
                this.setGrumpy((level.getBrightness(LightLayer.BLOCK, this.blockPosition()) == 0 && level.getBrightness(LightLayer.SKY, this.blockPosition()) >= 0)
                        || (level.getBrightness(LightLayer.BLOCK, this.blockPosition()) == 0 && level.isThundering()));
            } else {
                this.setGrumpy((level.getBrightness(LightLayer.BLOCK, this.blockPosition()) == 0 && level.getBrightness(LightLayer.SKY, this.blockPosition()) == 0)
                        || (level.getBrightness(LightLayer.BLOCK, this.blockPosition()) == 0 && level.isThundering()));
            }
        }
    }

    // Makes the Glare persistent and emit Glow Berry Dust particles when given Glow Berries

    @Override
    public void aiStep() {
        super.aiStep();
        if (level.getGameTime() % 20L == 0L) {
            updateGrumpy(this.level);
        }
        int berryAmount = this.getGlowberries();
        if (berryAmount > 0) {
            this.setPersistenceRequired();
            this.setGlowberries(berryAmount);
            this.level.addParticle(USParticleTypes.GLOWBERRY_DUST_PARTICLES.get(), this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
        }

        // Makes the Glare become persistent when leashed

        if (this.isLeashed()) {
            this.setPersistenceRequired();
            if (!this.getBlockStateOn().isAir()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01, 0.0D));
            }
        }
    }

    // Makes it so that the Glare cannot take fall damage

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, @NotNull DamageSource damageSource) {
        return false;
    }

    protected void checkFallDamage(double heightDifference, boolean onGround, @NotNull BlockState landedState, @NotNull BlockPos landedPosition) {
    }

    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluid) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    // Gets the offset for where Leads attach to the mob

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    // Allows the player to interact with a Glare using Glow Berries

    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResult actionResult = super.mobInteract(player, hand);
        if (actionResult.consumesAction()) {
            return actionResult;
        } else if (!this.level.isClientSide) {
            return GlareBrain.playerInteract(this, player, hand);
        } else {
            boolean bl = GlareBrain.isGlowBerry(player.getItemInHand(hand));
            return bl ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    // Sound Events

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isGrumpy() ? USSoundEvents.GLARE_GRUMPY_IDLE.get() : USSoundEvents.GLARE_IDLE.get();
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return USSoundEvents.GLARE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return USSoundEvents.GLARE_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.MOSS_STEP;
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(this.getStepSound(), 0.5F, 1.0F);
    }

    // Sets the eye height of the mob

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * (this.isBaby() ? 0.4f : 0.7f);
    }

    // Sets the amount of ticks for how long the Glare is grumpy

    private void setGrumpyTick(int ticks) {
        this.entityData.set(GRUMPY_TICKS, ticks);
    }

    public int getGrumpyTick() {
        return this.entityData.get(GRUMPY_TICKS);
    }

    // Allows the Glare to place Glow Berry Dust when given Glow Berries

    public void setLightblock(BlockPos pos) {
        BlockState blockState = USBlocks.GLOWBERRY_DUST.get().defaultBlockState();
        if (level.getBlockState(pos).isAir()) {
            level.setBlockAndUpdate(pos, blockState);
            this.playSound(USSoundEvents.GLOWBERRY_DUST_PLACE.get(), 1.0f, 1.0f);
        }
    }

    public void setGlowberries(int amount) {
        this.brain.setMemory(USMemoryModules.GLOWBERRIES_GIVEN.get(), amount);
        this.entityData.set(GLOWBERRIES_GIVEN, amount);
    }

    public int getGlowberries() {
        return this.entityData.get(GLOWBERRIES_GIVEN);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel world, @NotNull AgeableMob entity) {
        return null;
    }
}