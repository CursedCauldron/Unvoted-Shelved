package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;

//<>

public class FindDarkSpotTask extends Task<CreatureEntity> {
    private final float speed;
    private BlockPos darkPos;

    public FindDarkSpotTask(float speed) {
        super(ImmutableMap.of(MemoryModuleType.AVOID_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED));
        this.speed = speed;
    }

    @Override
    protected boolean shouldExecute(ServerWorld worldIn, CreatureEntity owner) {
        return this.ableToFindDarkness(worldIn, owner) && owner.isAlive();
    }

    @Override
    protected boolean shouldContinueExecuting(ServerWorld world, CreatureEntity entity, long gameTime) {
        return this.ableToFindDarkness(world, entity) && entity.isAlive() && !((GlareEntity)entity).isGrumpy();
    }

    @Override
    protected void updateTask(ServerWorld world, CreatureEntity entity, long gameTime) {
        super.updateTask(world, entity, gameTime);
        if (this.ableToFindDarkness(world, entity)) {
            entity.getNavigator().tryMoveToXYZ(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ(), this.speed);
        }
    }

    @Override
    protected void resetTask(ServerWorld world, CreatureEntity entity, long gameTime) {
        super.resetTask(world, entity, gameTime);
        this.darkPos = null;
        ((GlareEntity)entity).setGrumpy(world.getLightFor(LightType.BLOCK, entity.getPosition()) == 0 || world.getLightFor(LightType.SKY, entity.getPosition()) == 0);
    }

    private boolean ableToFindDarkness(ServerWorld worldIn, CreatureEntity entity) {
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -8; y <= 8; y++) {
                    BlockPos entityPos = entity.getPosition();
                    BlockPos blockPos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                    if (entityPos.withinDistance(blockPos, 8)) {
                        if (worldIn.getLightFor(LightType.BLOCK, blockPos) == 0 || worldIn.getLightFor(LightType.SKY, blockPos) == 0) {
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