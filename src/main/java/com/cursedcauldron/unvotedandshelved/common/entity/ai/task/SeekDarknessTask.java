package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING;
import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.GIVEN_GLOWBERRY;

//<>

public class SeekDarknessTask extends Behavior<GlareEntity> {
    private final int range;
    private final float speed;
    private BlockPos darkPos;
    private int wantedX;
    private int wantedY;
    private int wantedZ;

    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(DATA_GLARE_DARK_TICKS_REMAINING, MemoryStatus.VALUE_PRESENT), 1000);
        System.out.println("Darkness task initiated!");
        this.range = range;
        this.speed = speed;
    }

    protected boolean checkExtraStartConditions(ServerLevel worldIn, GlareEntity owner) {
        System.out.println("Checking for start conditions...");
        return !owner.isInWaterOrBubble() && owner.getBrain().hasMemoryValue(DATA_GLARE_DARK_TICKS_REMAINING);
    }

    protected boolean canStillUse(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Checking if can use..");
        return glare.getBrain().hasMemoryValue(DATA_GLARE_DARK_TICKS_REMAINING) && !glare.getNavigation().isDone();
    }

    protected boolean getDarkPos(ServerLevel level, GlareEntity glare) {
        System.out.println("Getting dark pos...");
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -8; y <= 8; y++) {
                    BlockPos entityPos = glare.blockPosition();
                    BlockPos blockPos2 = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                    if (entityPos.closerThan(blockPos2, range)) {
                        if ((level.isInWorldBounds(blockPos2) && level.isEmptyBlock(blockPos2) && level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.LAND) && !level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.WATER)) &&
                                ((level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getBrightness(LightLayer.SKY, blockPos2) == 0) ||
                                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getDayTime() >= 13000) ||
                                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.isThundering()))) {
                            System.out.println(blockPos2);
                            this.darkPos = blockPos2;
                            return true;
                        }
                    }  else return false;
                }
            }
        }
        return false;
    }

    @Override
    protected void tick(ServerLevel level, GlareEntity entity, long time) {
        super.tick(level, entity, time);
        if (this.getDarkPos(level, entity)) {
            boolean darkArea = this.getDarkPos(level, entity);
            if (darkArea) {
                entity.getNavigation().moveTo(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ(), this.speed);
            } else {
                this.finishFinding(entity);
            }
        }
    }

    protected void finishFinding(GlareEntity entity) {
        Brain<GlareEntity> brain = entity.getBrain();
        System.out.println("Stopping seeking!");
        entity.setFindingDarkness(false);
        brain.setMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING, 0);
        brain.eraseMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING);
        brain.eraseMemory(UnvotedAndShelved.GIVEN_GLOWBERRY);
        brain.useDefaultActivity();
    }

    protected void stop(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Stopping!");
        this.finishFinding(glare);
    }

    protected void start(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Starting!");
    }
}