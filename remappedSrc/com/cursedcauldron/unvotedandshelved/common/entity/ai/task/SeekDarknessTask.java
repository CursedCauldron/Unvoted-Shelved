package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

//<>

public class SeekDarknessTask extends Task<GlareEntity> {
    private final int range;
    private final float speed;
    private BlockPos darkPos;

    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean shouldRun(ServerWorld worldIn, GlareEntity owner) {
        return this.ableToFindDarkness(worldIn, owner) && owner.isAlive();
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, GlareEntity entity, long time) {
        return this.ableToFindDarkness(world, entity) && entity.isAlive() && !entity.isGrumpy() && !entity.isInsideWaterOrBubbleColumn();
    }

    @Override
    protected void keepRunning(ServerWorld world, GlareEntity entity, long time) {
        super.keepRunning(world, entity, time);
        if (this.ableToFindDarkness(world, entity)) {
            entity.getNavigation().startMovingTo(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ(), this.speed);
        }
    }

    protected void finishRunning(ServerWorld world, GlareEntity entity, long time) {
        super.finishRunning(world, entity, time);
        this.darkPos = null;
    }

    private boolean ableToFindDarkness(ServerWorld worldIn, GlareEntity entity) {
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -8; y <= 8; y++) {
                    BlockPos entityPos = entity.getBlockPos();
                    BlockPos blockPos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                    if (entityPos.isWithinDistance(blockPos, 8)) {
                        if (worldIn.getLightLevel(LightType.BLOCK, blockPos) == 0 && worldIn.getLightLevel(LightType.SKY, blockPos) == 0) {
                            this.darkPos = blockPos;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}