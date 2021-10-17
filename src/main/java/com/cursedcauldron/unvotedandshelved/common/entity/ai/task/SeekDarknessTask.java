package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

//<>

public class SeekDarknessTask extends Task<GlareEntity> {
    private final int range;
    private final float speed;
    private long time;

    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected void resetTask(ServerWorld world, GlareEntity entity, long gameTime) {
        this.time = gameTime + 20L + 2L;
    }

    @Override
    protected boolean shouldExecute(ServerWorld world, GlareEntity entity) {
        return !entity.world.getFluidState(entity.getPosition()).isTagged(FluidTags.WATER);
    }

    @Override
    protected void startExecuting(ServerWorld world, GlareEntity entity, long gameTime) {
        if (gameTime >= this.time) {
            BlockPos currentPos = null;
            BlockPos waterPos = null;
            BlockPos entityPos = entity.getPosition();
            Iterable<BlockPos> proximitySortedBoxPositions = BlockPos.getProximitySortedBoxPositionsIterator(entityPos, this.range, this.range, this.range);

            for (BlockPos positions : proximitySortedBoxPositions) {
                if (positions.getX() != entityPos.getX() || positions.getZ() != entityPos.getZ()) {
                    BlockState aboveState = entity.world.getBlockState(positions.up());
                    BlockState currentState = entity.world.getBlockState(positions);
                    if (currentState.getLightValue() != 0) {
                        if (aboveState.isAir()) {
                            currentPos = positions.toImmutable();
                            break;
                        }

                        if (waterPos == null && !positions.withinDistance(entity.getPositionVec(), 1.5D)) {
                            waterPos = positions.toImmutable();
                        }
                    }
                }
            }

            if (currentPos == null) {
                currentPos = waterPos;
            }

            if (currentPos != null) {
                this.time = gameTime + 40L;
                BrainUtil.setTargetPosition(entity, currentPos, this.speed, 0);
            }
        }
    }
}