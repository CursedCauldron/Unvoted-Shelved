package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.ai.GlareTasks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;

//<>

public class GlareEntity extends CreatureEntity implements IFlyingAnimal {
    protected static final ImmutableList<SensorType<? extends Sensor<? super GlareEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.AVOID_TARGET);
    private static final DataParameter<Boolean> GRUMPY = EntityDataManager.createKey(GlareEntity.class, DataSerializers.BOOLEAN);

    public GlareEntity(EntityType<? extends GlareEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 20, true);
        this.lookController = new LookController(this);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.COCOA, -1.0F);
        this.setPathPriority(PathNodeType.FENCE, -1.0F);
    }

    @Override
    protected Brain.BrainCodec<GlareEntity> getBrainCodec() {
        return Brain.createCodec(MEMORY_MODULES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> createBrain(Dynamic<?> dynamicIn) {
        return GlareTasks.create(this.getBrainCodec().deserialize(dynamicIn));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("IsGrumpy", this.isGrumpy());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setGrumpy(compound.getBoolean("IsGrumpy"));
    }

    public boolean isInAir() {
        return this.world.getBlockState(this.getPosition()).isAir();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(GRUMPY, false);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new GlareEntity.WanderGoal());
//        this.goalSelector.addGoal(2, new SwimGoal(this));
//        this.goalSelector.addGoal(5, new GlareEntity.FindDarkSpaceGoal(this, 0.6F));
    }

    @Override
    public Brain<GlareEntity> getBrain() {
        return (Brain<GlareEntity>)super.getBrain();
    }

    @Override
    protected void updateAITasks() {
        this.world.getProfiler().startSection("glareBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().endSection();
        this.world.getProfiler().startSection("glareActivityUpdate");
        GlareTasks.updateActivities(this);
        this.world.getProfiler().endSection();
        super.updateAITasks();
    }

    public void setGrumpy(boolean isGrumpy) {
        this.dataManager.set(GRUMPY, isGrumpy);
    }

    public boolean isGrumpy() {
        return this.dataManager.get(GRUMPY);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 10.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6F).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3F).createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPacketSender.sendLivingEntity(this);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator navigator = new FlyingPathNavigator(this, worldIn) {
            @Override
            public boolean canEntityStandOnPos(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };

        navigator.setCanOpenDoors(false);
        navigator.setCanSwim(true);
        navigator.setCanEnterDoors(true);
        return navigator;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    class WanderGoal extends Goal {
        public WanderGoal() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
//            return GlareEntity.this.navigator.noPath();
            return GlareEntity.this.navigator.noPath() && GlareEntity.this.rand.nextInt(10) == 0;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return GlareEntity.this.navigator.hasPath();
        }

        @Override
        public void startExecuting() {
            Vector3d vector3d = this.getRandomLocation();
            if (vector3d != null) {
                GlareEntity.this.navigator.setPath(GlareEntity.this.navigator.getPathToPos(new BlockPos(vector3d), 1), 1.0D);
            }
        }

        private Vector3d getRandomLocation() {
            Vector3d vector3d = GlareEntity.this.getLook(0.0F);

            Vector3d vector3d2 = RandomPositionGenerator.findAirTarget(GlareEntity.this, 8, 7, vector3d, ((float)Math.PI / 2F), 2, 1);
            return vector3d2 != null ? vector3d2 : RandomPositionGenerator.findGroundTarget(GlareEntity.this, 8, 4, -2, vector3d, (float)Math.PI / 2F);
        }
    }

    private static class FindDarkSpaceGoal extends Goal {
        private final GlareEntity entity;
        private final double speed;
        private final World world;
        private BlockPos darkPos;

        public FindDarkSpaceGoal(GlareEntity entity, double speed) {
            this.entity = entity;
            this.world = this.entity.world;
            this.speed = speed;

            entity.setGrumpy(this.darkPos != null);
        }

        @Override
        public boolean shouldExecute() {
            return this.ableToFindDarkness() && this.entity.isAlive();
        }

        private boolean ableToFindDarkness() {
            for (int x = -8; x <= 8; x++) {
                for (int z = -8; z <= 8; z++) {
                    for (int y = -8; y <= 8; y++) {
                        BlockPos entityPos = this.entity.getPosition();
                        BlockPos blockPos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                        if (entityPos.withinDistance(blockPos, 8)) {
                            if (this.world.getLightFor(LightType.BLOCK, blockPos) == 0) {
                                this.darkPos = blockPos;
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.ableToFindDarkness() && this.entity.isAlive();
        }

        @Override
        public void tick() {
            super.tick();
            if (ableToFindDarkness()) {
                this.entity.getNavigator().tryMoveToXYZ(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ(), this.speed);
            }
        }

        @Override
        public void resetTask() {
            super.resetTask();
            this.darkPos = null;
        }
    }
}