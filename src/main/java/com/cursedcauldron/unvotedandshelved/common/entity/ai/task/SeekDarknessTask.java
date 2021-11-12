package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING;

//<>

public class SeekDarknessTask extends Behavior<GlareEntity> {
    private final int range;
    private final float speed;
    private BlockPos darkPos;
    private int wantedX;
    private int wantedY;
    private int wantedZ;

    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(DATA_GLARE_DARK_TICKS_REMAINING, MemoryStatus.VALUE_PRESENT));
        System.out.println("Darkness task initiated!");
        this.range = range;
        this.speed = speed;
    }

    protected boolean checkExtraStartConditions(ServerLevel worldIn, GlareEntity owner) {
        System.out.println("Checking start conditions...");
        long time = worldIn.getGameTime();
        return setWantedPos(worldIn, owner, time);
    }

    protected boolean canStillUse(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Checking if can use..");
        return !glare.isInWaterOrBubble() && glare.getBrain().hasMemoryValue(DATA_GLARE_DARK_TICKS_REMAINING) && !glare.getNavigation().isDone();
    }

    protected boolean getDarkPos(ServerLevel level, GlareEntity glare) {
        Random random = glare.getRandom();
        System.out.println("Getting dark pos...");
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos entityPos = glare.blockPosition();
                    BlockPos blockPos2 = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                    if (entityPos.closerThan(blockPos2, range)) {
                        if ((level.isInWorldBounds(blockPos2) && level.isEmptyBlock(blockPos2) && level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.LAND)) &&
                                ((level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getBrightness(LightLayer.SKY, blockPos2) == 0) ||
                                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getDayTime() >= 13000) ||
                                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.isThundering()))) {
                            System.out.println(blockPos2);
                            this.darkPos = blockPos2;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    protected boolean setWantedPos(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Setting wanted pos...");
        boolean vec3 = this.getDarkPos(level, glare);
        if (!vec3) {
            doStop(level, glare, time);
            return false;
        } else {
            this.wantedX = this.darkPos.getX();
            this.wantedY = this.darkPos.getY();
            this.wantedZ = this.darkPos.getZ();
            return true;
        }
    }

    protected void stop(ServerLevel world, GlareEntity entity, long time) {
        System.out.println("Stopping!");
        entity.setDarkTicks(0);
        entity.setFindingDarkness(false);
    }

    protected void start(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Starting!");
        glare.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speed);
    }
}