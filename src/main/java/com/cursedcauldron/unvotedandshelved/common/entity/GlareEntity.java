package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.ai.GlareBrain;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.Optional;
import java.util.Random;

//<>

public class GlareEntity extends PassiveEntity implements Flutterer {
    protected static final ImmutableList<SensorType<? extends Sensor<? super GlareEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.BREED_TARGET, UnvotedAndShelved.SEEK_TICKS, UnvotedAndShelved.GRUMPY_TICKS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.AVOID_TARGET);
    private static final TrackedData<Boolean> GRUMPY = DataTracker.registerData(GlareEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FINDING_DARKNESS = DataTracker.registerData(GlareEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> GRUMPY_TICKS;

    private float currentPitch;
    private float lastPitch;

    public GlareEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.lookControl = new LookControl(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    @Override
    protected Brain.Profile<GlareEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return GlareBrain.create(this, this.createBrainProfile().deserialize(dynamic));
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(GRUMPY, false);
        this.dataTracker.startTracking(FINDING_DARKNESS, false);
        this.dataTracker.startTracking(GRUMPY_TICKS, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsGrumpy", this.isGrumpy());
        nbt.putInt("GrumpyTicks", this.getGrumpyTick());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setGrumpy(nbt.getBoolean("IsGrumpy"));
        this.setGrumpyTick(nbt.getInt("GrumpyTicks"));
    }

    @Override
    public boolean isInAir() {
        return this.world.getBlockState(this.getBlockPos()).isAir();
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public Brain<GlareEntity> getBrain() {
        return (Brain<GlareEntity>)super.getBrain();
    }

    @Override
    protected void mobTick() {
        this.world.getProfiler().push("glareBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().pop();
        this.world.getProfiler().push("glareActivityUpdate");
        GlareBrain.updateActivities(this);
        this.world.getProfiler().pop();
        super.mobTick();
        Optional<Integer> optional = this.getBrain().getOptionalMemory(UnvotedAndShelved.SEEK_TICKS);
        this.setFindingDarkness(optional.isPresent() && optional.get() > 0);
    }

    public void setGrumpy(boolean isGrumpy) {
        this.dataTracker.set(GRUMPY, isGrumpy);
    }

    public boolean isGrumpy() {
        return this.dataTracker.get(GRUMPY);
    }

    public void setFindingDarkness(boolean findingDarkness) {
        this.dataTracker.set(FINDING_DARKNESS, findingDarkness);
    }


    public static DefaultAttributeContainer.Builder createGlareAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.BLOCK_MOSS_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.5F, 1.0F);
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation navigator = new BirdNavigation(this, world) {
            @Override
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        navigator.setCanPathThroughDoors(false);
        navigator.setCanSwim(false);
        navigator.setCanEnterOpenDoors(true);
        return navigator;
    }

    @Override

    public void tickMovement() {
        super.tickMovement();
        int i = this.getGrumpyTick();
        if (i > 0) {
            this.setGrumpyTick(i - 1);
        }
        GlareEntity entity = this;
        entity.setGrumpy((world.getLightLevel(LightType.BLOCK, entity.getBlockPos()) == 0 && world.getLightLevel(LightType.SKY, entity.getBlockPos()) == 0) || (world.getLightLevel(LightType.BLOCK, entity.getBlockPos()) == 0 && world.getTimeOfDay() >= 13000));
    }


    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    protected void swimUpward(Tag<Fluid> fluid) {
        this.setVelocity(this.getVelocity().add(0.0D, 0.01D, 0.0D));
    }

    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, 0.5F * this.getStandingEyeHeight(), this.getWidth() * 0.2F);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ActionResult actionResult = super.interactMob(player, hand);
        if (actionResult.isAccepted()) {
            return actionResult;
        } else if (!this.world.isClient) {
            return GlareBrain.playerInteract(this, player, hand);
        } else {
            boolean bl = GlareBrain.isGlowBerry(this, player.getStackInHand(hand));
            return bl ? ActionResult.SUCCESS : ActionResult.PASS;
        }
    }

    public void setParticle(){
        this.world.addParticle(ParticleTypes.GLOW, getParticleX(0.6D), this.getRandomBodyY(), this.getParticleZ(0.6D), 0.0D, 0.0D, 0.0D);
    }


    private void setGrumpyTick(int ticks) {
        this.dataTracker.set(GRUMPY_TICKS, ticks);
    }

    public int getGrumpyTick() {
        return this.dataTracker.get(GRUMPY_TICKS);
    }

    static {
        GRUMPY_TICKS = DataTracker.registerData(GlareEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }


}