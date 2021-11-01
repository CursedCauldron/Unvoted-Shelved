package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;

import static com.cursedcauldron.unvotedandshelved.common.entity.ai.GlareBrain.addIdleActivities;
import static com.cursedcauldron.unvotedandshelved.common.entity.ai.GlareBrain.updateActivities;

//<>

public class SeekDarknessTask extends Behavior<GlareEntity> {
    private final int range;
    private final float speed;
    private BlockPos darkPos;

    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.AVOID_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, GlareEntity owner) {
        return this.ableToFindDarkness(worldIn, owner) && owner.isAlive();
    }


    @Override
    protected boolean canStillUse(ServerLevel world, GlareEntity entity, long time) {
        return (entity.getDarkTicksRemaining() > 0);
    }

    @Override
    protected void tick(ServerLevel world, GlareEntity entity, long time) {
        super.tick(world, entity, time);
        if (this.ableToFindDarkness(world, entity)) {
            entity.getNavigation().moveTo(this.darkPos.getX(), this.darkPos.getY() + 3, this.darkPos.getZ(), this.speed);
        }
    }

    protected void stop(ServerLevel world, GlareEntity entity, long time) {
        super.stop(world, entity, time);
        darkPos = null;
        entity.setDarkTicks(0);
        updateActivities(entity);
    }

    private boolean ableToFindDarkness(ServerLevel worldIn, GlareEntity entity) {
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -8; y <= 8; y++) {
                    BlockPos entityPos = entity.blockPosition();
                    BlockPos blockPos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                    if (entityPos.closerThan(blockPos, 8)) {
                        if (worldIn.getBrightness(LightLayer.BLOCK, blockPos) == 0 && worldIn.getBrightness(LightLayer.SKY, blockPos) == 0) {
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