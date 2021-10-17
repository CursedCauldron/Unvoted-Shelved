package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

//<>

public class SeekDarknessTask extends Task<GlareEntity> {
    private final int range;
    private final float speed;
    private long time;

    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected void finishRunning(ServerWorld world, GlareEntity entity, long time) {
        this.time = time + 20L + 2L;
    }

    @Override
    protected boolean shouldRun(ServerWorld serverWorld, GlareEntity entity) {
        return !entity.isGrumpy();
    }

    @Override
    protected void run(ServerWorld serverWorld, GlareEntity entity, long time) {
        if (time >= this.time) {
            BlockPos currentPos = null;
            BlockPos darknessPos = null;
            BlockPos entityPos = entity.getBlockPos();
            Iterable<BlockPos> iterateOutwards = BlockPos.iterateOutwards(entityPos, this.range, this.range, this.range);

            for (BlockPos positions : iterateOutwards) {
                if (positions.getX() != entityPos.getX() || positions.getZ() != entityPos.getZ()) {
                    BlockState aboveState = entity.world.getBlockState(positions.up());
                    BlockState currentState = entity.world.getBlockState(positions);
                    if (currentState.getLuminance() == 0) {
                        if (aboveState.isAir()) {
                            currentPos = positions.toImmutable();
                            break;
                        }

                        if (darknessPos == null && !positions.isWithinDistance(entity.getPos(), 1.5D)) {
                            darknessPos = positions.toImmutable();
                        }
                    }
                }
            }

            if (currentPos == null) {
                currentPos = darknessPos;
            }

            if (currentPos != null) {
                this.time = time + 40L;
                LookTargetUtil.walkTowards(entity, currentPos, this.speed, 0);
            }
        }
    }
}