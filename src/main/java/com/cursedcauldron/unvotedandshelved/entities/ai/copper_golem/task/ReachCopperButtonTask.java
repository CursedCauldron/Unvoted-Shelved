package com.cursedcauldron.unvotedandshelved.entities.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.block.WeatheringCopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ReachCopperButtonTask extends Behavior<CopperGolemEntity> {
    private int remainingCooldown;
    @Nullable
    private Path path;
    @Nullable
    private BlockPos lastTargetPos;
    private float speedModifier;

    public ReachCopperButtonTask() {
        super(ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT, USMemoryModules.COPPER_BUTTON.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, CopperGolemEntity entity) {
        if (this.remainingCooldown > 0) {
            --this.remainingCooldown;
            return false;
        } else {
            Brain<?> brain = entity.getBrain();
            WalkTarget walktarget = brain.getMemory(MemoryModuleType.WALK_TARGET).get();
            boolean flag = this.reachedTarget(entity, walktarget);
            if (!flag && this.tryComputePath(entity, walktarget, world.getGameTime())) {
                this.lastTargetPos = walktarget.getTarget().currentBlockPosition();
                return true;
            } else {
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                if (flag) {
                    brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                }

                return false;
            }
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel world, CopperGolemEntity entity, long p_23588_) {
        if (this.path != null && this.lastTargetPos != null) {
            Optional<WalkTarget> optional = entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET);
            PathNavigation pathnavigation = entity.getNavigation();
            return !pathnavigation.isDone() && optional.isPresent() && !this.reachedTarget(entity, optional.get());
        } else {
            return false;
        }
    }

    @Override
    protected void stop(ServerLevel world, CopperGolemEntity entity, long p_23603_) {
        if (entity.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET) && !this.reachedTarget(entity, entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get()) && entity.getNavigation().isStuck()) {
            this.remainingCooldown = world.getRandom().nextInt(40);
        }

        entity.getNavigation().stop();
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        entity.getBrain().eraseMemory(MemoryModuleType.PATH);
        this.path = null;
    }

    @Override
    protected void start(ServerLevel world, CopperGolemEntity entity, long p_23611_) {
        BlockPos buttonPos = getButtonPos(entity);
        entity.getBrain().setMemory(MemoryModuleType.PATH, this.path);
        entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(buttonPos), 0.5F, 1));
        entity.getNavigation().moveTo(this.path, this.speedModifier);
    }

    public BlockPos getButtonPos(CopperGolemEntity entity) {
        List<BlockPos> lists = Lists.newArrayList();
        int radius = 8;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos blockPos = new BlockPos(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                    if (entity.level.getBlockState(blockPos).getBlock() instanceof WeatheringCopperButtonBlock) {
                        lists.add(blockPos);
                    }
                }
            }
        }
        if (lists.isEmpty()) return null;

        return lists.get(entity.getRandom().nextInt(lists.size()));
    }

    @Override
    protected void tick(ServerLevel world, CopperGolemEntity entity, long p_23619_) {
        Path path = entity.getNavigation().getPath();
        Brain<?> brain = entity.getBrain();
        if (this.path != path) {
            this.path = path;
            brain.setMemory(MemoryModuleType.PATH, path);
        }

        if (path != null && this.lastTargetPos != null) {
            WalkTarget walktarget = brain.getMemory(MemoryModuleType.WALK_TARGET).get();
            if (walktarget.getTarget().currentBlockPosition().distSqr(this.lastTargetPos) > 4.0D && this.tryComputePath(entity, walktarget, world.getGameTime())) {
                this.lastTargetPos = walktarget.getTarget().currentBlockPosition();
                this.start(world, entity, p_23619_);
            }

        }
    }

    private boolean tryComputePath(Mob mob, WalkTarget target, long p_23595_) {
        BlockPos blockpos = target.getTarget().currentBlockPosition();
        this.path = mob.getNavigation().createPath(blockpos, 0);
        this.speedModifier = target.getSpeedModifier();
        Brain<?> brain = mob.getBrain();
        if (this.reachedTarget(mob, target)) {
            brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        } else {
            boolean flag = this.path != null && this.path.canReach();
            if (flag) {
                brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            } else if (!brain.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
                brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, p_23595_);
            }

            if (this.path != null) {
                return true;
            }

            Vec3 vec3 = DefaultRandomPos.getPosTowards((PathfinderMob)mob, 10, 7, Vec3.atBottomCenterOf(blockpos), (double)((float)Math.PI / 2F));
            if (vec3 != null) {
                this.path = mob.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }

        return false;
    }

    private boolean reachedTarget(Mob mob, WalkTarget target) {
        return target.getTarget().currentBlockPosition().distManhattan(mob.blockPosition()) <= target.getCloseEnoughDist();
    }
}
