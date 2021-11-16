package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.Objects;
import java.util.Optional;


//<>

public class GlowberryStrollTask extends Behavior<GlareEntity> {
    private BlockPos darkPos;
    private final int range;
    private final float speed;

    public GlowberryStrollTask(int range, float speed) {
        super(ImmutableMap.of(UnvotedAndShelved.GLOWBERRIES_GIVEN, MemoryStatus.VALUE_PRESENT));
        System.out.println("Darkness task initiated!");
        this.range = range;
        this.speed = speed;
    }
    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, GlareEntity owner) {
        System.out.println("Checking start conditions...");
        System.out.println(owner.getBrain().checkMemory(UnvotedAndShelved.DARK_POS, MemoryStatus.VALUE_PRESENT));
        return !owner.isInWaterOrBubble() && (owner.getBrain().getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get() >= 1);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Checking can still use...");
        return (glare.getBrain().getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get() >= 1);
    }



    protected boolean getDarkPos(ServerLevel level, GlareEntity glare) {
        if (this.darkPos == null) {
            System.out.println("Getting dark pos...");
            for (int x = -8; x <= 8; x++) {
                for (int z = -8; z <= 8; z++) {
                    for (int y = -1; y <= 1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              ; y++) {
                        BlockPos entityPos = glare.blockPosition();
                        BlockPos blockPos2 = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                        if (entityPos.closerThan(blockPos2, range)) {
                            if ((level.isInWorldBounds(blockPos2) && level.isEmptyBlock(blockPos2) && level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.LAND) && !level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.WATER)) &&
                                    ((level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getBrightness(LightLayer.SKY, blockPos2) == 0) ||
                                            (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.isNight()) ||
                                            (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.isThundering()))) {
                                System.out.println(blockPos2);
                                glare.getBrain().setMemory(UnvotedAndShelved.DARK_POS, blockPos2);
                                this.darkPos = blockPos2;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    @Override
    protected void tick(ServerLevel level, GlareEntity entity, long time) {
        System.out.println("Ticking...");
        super.tick(level, entity, time);
        if (this.getDarkPos(level, entity)) {
            System.out.println("Navigating");
            Brain<GlareEntity> brain = entity.getBrain();
            System.out.println("Found a dark spot!");
            entity.getNavigation().stop();
            int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
            if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY() + 2, this.darkPos.getZ());
                entity.getNavigation().moveTo(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ(), speed);
                if ((entity.blockPosition().getX() == this.darkPos.getX()) && (entity.blockPosition().getZ() == this.darkPos.getZ())) {
                    entity.setLightblock(blockPos);
                    entity.setGlowberries(i - 1);
                    this.darkPos = null;
                }
            }
        }
    }

    @Override
    protected void stop(ServerLevel level, GlareEntity entity, long time) {
        System.out.println("Stopping!");
        if (this.darkPos != null) {
        System.out.println("Navigating");
            Brain<GlareEntity> brain = entity.getBrain();
            System.out.println("Found a dark spot!");
            entity.getNavigation().stop();
            int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
            if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY() + 1, this.darkPos.getZ());
                entity.getNavigation().moveTo(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ(), speed);
                if ((entity.blockPosition().getX() == this.darkPos.getX()) && (entity.blockPosition().getZ() == this.darkPos.getZ())) {
                    entity.setLightblock(blockPos);
                    entity.setGlowberries(i - 1);
                    this.darkPos = null;
                }
            }
        }
    }

    @Override
    protected void start(ServerLevel level, GlareEntity entity, long time) {
        System.out.println("Starting!");
        this.getDarkPos(level, entity);
        if (this.darkPos != null) {
            System.out.println("Navigating");
            Brain<GlareEntity> brain = entity.getBrain();
            System.out.println("Found a dark spot!");
            entity.getNavigation().stop();
            int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
            if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ());
                entity.getNavigation().moveTo(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ(), speed);
                if ((entity.blockPosition().getX() == this.darkPos.getX()) && (entity.blockPosition().getZ() == this.darkPos.getZ())) {
                    entity.setLightblock(blockPos);
                    entity.setGlowberries(i - 1);
                    this.darkPos = null;
                }
            }
        }
    }
}
