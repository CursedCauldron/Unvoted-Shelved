package com.cursedcauldron.unvotedandshelved.entities;

import com.cursedcauldron.unvotedandshelved.entities.ai.glare.GlareBrain;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.init.USSensorTypes;
import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GlareEntity extends AgeableMob implements FlyingAnimal {
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.GLOW_BERRIES);
    protected static final ImmutableList<SensorType<? extends Sensor<? super GlareEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS, USSensorTypes.GLARE_TEMPTATIONS.get());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(USMemoryModules.GLOWBERRIES_GIVEN.get(), USMemoryModules.GRUMPY_TICKS.get(), USMemoryModules.DARK_TICKS_REMAINING.get(), MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.AVOID_TARGET);
    private static final EntityDataAccessor<Boolean> GRUMPY = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHINY = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FINDING_DARKNESS = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> GRUMPY_TICKS;
    private static final EntityDataAccessor<Integer> GLOWBERRIES_GIVEN;

    public GlareEntity(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 5, true);
        this.lookControl = new LookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
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
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return GlareBrain.create(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GRUMPY, false);
        this.entityData.define(FINDING_DARKNESS, false);
        this.entityData.define(SHINY, false);
        this.entityData.define(GRUMPY_TICKS, 0);
        this.entityData.define(GLOWBERRIES_GIVEN, 0);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return super.shouldDespawnInPeaceful() || this.getBrain().hasMemoryValue(USMemoryModules.GIVEN_GLOWBERRY.get());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsGrumpy", this.isGrumpy());
        tag.putInt("GrumpyTicks", this.getGrumpyTick());
        tag.putBoolean("IsShiny", this.isShiny());
        if (this.brain.getMemory(USMemoryModules.DARK_TICKS_REMAINING.get()).isPresent()) {
            tag.putInt("FindDarknessTicks", this.brain.getMemory(USMemoryModules.DARK_TICKS_REMAINING.get()).get());
        }
        if (this.brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).isPresent()) {
            tag.putInt("GlowberriesGiven", this.brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setGrumpy(tag.getBoolean("IsGrumpy"));
        this.setShiny(tag.getBoolean("IsShiny"));
        this.setGrumpyTick(tag.getInt("GrumpyTicks"));
        this.setGlowberries(tag.getInt("GlowberriesGiven"));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.MOSS_STEP, 0.5F, 1.0F);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation pathNavigation = new FlyingPathNavigation(this, world) {
            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below()).isAir();
            }
        };
        pathNavigation.setCanPassDoors(false);
        pathNavigation.setCanFloat(true);
        pathNavigation.setCanOpenDoors(true);
        return pathNavigation;
    }

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

    @Override
    public void aiStep() {
        super.aiStep();
        this.getBrain().getMemory(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS).ifPresent(System.out::println);
        if (level.getGameTime() % 20L == 0L) {
            updateGrumpy(this.level);
        }
        int berryAmount = this.getGlowberries();
        if (berryAmount > 0) {
            this.setPersistenceRequired();
            this.setGlowberries(berryAmount);
            this.level.addParticle(ParticleTypes.FALLING_SPORE_BLOSSOM, this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
        }
        if (this.isLeashed()) {
            this.setPersistenceRequired();
            if (!this.getBlockStateOn().isAir()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01, 0.0D));
            }
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> p_204045_) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult actionResult = super.mobInteract(player, hand);
        if (actionResult.consumesAction()) {
            return actionResult;
        } else if (!this.level.isClientSide()) {
            return GlareBrain.playerInteract(this, player, hand);
        } else {
            Optional<Integer> memory = this.getBrain().getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get());
            boolean bl = (memory.isPresent() && memory.get() < 5) && GlareBrain.isGlowBerry(this, player.getItemInHand(hand));
            return bl ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isGrumpy() ? USSoundEvents.GLARE_GRUMPY_IDLE.get() : USSoundEvents.GLARE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.MOSS_STEP;
    }

    @Override
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
        GLOWBERRIES_GIVEN = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.INT);
    }

    public void setLightblock(BlockPos pos) {
        BlockState blockState = USBlocks.GLOWBERRY_DUST.get().defaultBlockState();
        if (level.getBlockState(pos).isAir()) {
            level.setBlockAndUpdate(pos, blockState);
            this.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 1.0f, 1.5f);
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
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return null;
    }

    @Override
    public boolean isFlying() {
        return this.level.getBlockState(this.blockPosition()).isAir();
    }
}