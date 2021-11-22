package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;


//<>

public class GlowberryStrollTask<E extends Mob> extends Behavior<GlareEntity> {
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
        return (glare.getBrain().getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get() >= 1) && (this.darkPos != null);
    }


    protected boolean getDarkPos(ServerLevel level, GlareEntity glare) {
        if (this.darkPos == null) {
            System.out.println("Getting dark pos...");
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    for (int y = -10; y <= 10; y++) {
                        BlockPos entityPos = glare.blockPosition();
                        BlockPos blockPos2 = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                        if (entityPos.closerThan(blockPos2, range)) {
                            if ((level.isInWorldBounds(blockPos2) && level.getBlockState(blockPos2).isAir() && level.isEmptyBlock(blockPos2)
                                    && level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.LAND)
                                    && !level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.WATER)) &&
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

    private static int getRandomOffset(Random random) {
        return random.nextInt(3) - 1;
    }

    private static BlockPos getNearbyPos(GlareEntity mob, BlockPos blockPos) {
        Random random = mob.level.random;
        return blockPos.offset(getRandomOffset(random), 0, getRandomOffset(random));
    }

    @Override
    protected void tick(ServerLevel level, GlareEntity entity, long time) {
        System.out.println("Ticking...");
        super.tick(level, entity, time);
        if (this.getDarkPos(level, entity)) {
            System.out.println("Navigating");
            Brain<GlareEntity> brain = entity.getBrain();
            if ((level.isInWorldBounds(darkPos) && level.getBlockState(darkPos).isAir() && level.isEmptyBlock(darkPos) && level.getBlockState(darkPos).isPathfindable(level, darkPos, PathComputationType.LAND) && !level.getBlockState(darkPos).isPathfindable(level, darkPos, PathComputationType.WATER)) &&
                    ((level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.getBrightness(LightLayer.SKY, darkPos) == 0) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isNight()) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isThundering()))) {
                System.out.println("Found a dark spot!");
                entity.getNavigation().stop();
                int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                    BlockPos blockPos = new BlockPos(this.darkPos.getX(), (this.darkPos.getY() + 4), this.darkPos.getZ());
                        BehaviorUtils.setWalkAndLookTargetMemories(entity, getNearbyPos(entity, blockPos), this.speed, this.range);
                        if (entity.blockPosition().closerThan(darkPos, 3)) {
                            entity.setLightblock(blockPos);
                            entity.setGlowberries(i - 1);
                            this.darkPos = null;
                    }
                }
            } else {
                this.darkPos = null;
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
                    BehaviorUtils.setWalkAndLookTargetMemories(entity, getNearbyPos(entity, blockPos), this.speed, this.range);
                if (entity.blockPosition().closerThan(darkPos, 3)) {
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
                    BehaviorUtils.setWalkAndLookTargetMemories(entity, getNearbyPos(entity, blockPos), this.speed, this.range);
                    if (entity.blockPosition().closerThan(darkPos, 3)) {
                        entity.setLightblock(blockPos);
                        entity.setGlowberries(i - 1);
                        this.darkPos = null;
                }
            }
        }
    }
}