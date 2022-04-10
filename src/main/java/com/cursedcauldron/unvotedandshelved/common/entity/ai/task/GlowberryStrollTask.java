package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.google.common.collect.ImmutableMap;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;


//<>

public class GlowberryStrollTask extends Behavior<GlareEntity> {
    private BlockPos darkPos;
    private final int range;
    private final float speed;
    protected GroundPathNavigation groundNavigation;

    public GlowberryStrollTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, GlareEntity owner) {
        return !owner.isInWaterOrBubble() && (owner.getBrain().getMemory(USMemoryModules.GLOWBERRIES_GIVEN).get() >= 1);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, GlareEntity glare, long time) {
        return (glare.getBrain().getMemory(USMemoryModules.GLOWBERRIES_GIVEN).get() >= 1) && (this.darkPos != null);
    }

    private boolean pathfindDirectlyTowards(BlockPos blockPos, GlareEntity entity) {
        entity.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
        return entity.getNavigation().getPath() != null && entity.getNavigation().getPath().canReach();
    }

    private boolean isValidSpawnPos(BlockPos blockPos, ServerLevel level) {
        return !level.getBlockState(blockPos).is(Blocks.VINE) && !level.getBlockState(blockPos).is(BlockTags.LEAVES) &&  !level.getBlockState(blockPos).is(BlockTags.CAVE_VINES) && !level.getBlockState(blockPos).isAir() && !level.getFluidState(blockPos).is(Fluids.WATER) && !level.getFluidState(blockPos).is(Fluids.FLOWING_WATER);
    }

    protected void getDarkPos(ServerLevel level, GlareEntity glare) {
        if (this.darkPos == null) {
            for (int x = getRandomNumber(0, -range); x <= getRandomNumber(0, range); x++) {
                for (int z = getRandomNumber(0, -range); z <= getRandomNumber(0, range); z++) {
                    for (int y = getRandomNumber(0, -range); y <= getRandomNumber(0, range); y++) {
                        BlockPos entityPos = glare.blockPosition();
                        BlockPos blockPos2 = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                        BlockPos spacePos = blockPos2.below();
                        BlockPos groundPos = spacePos.below();
                        if ((level.isInWorldBounds(blockPos2) && isValidSpawnPos(groundPos, level) && level.isEmptyBlock(spacePos) && level.isEmptyBlock(blockPos2) && (level.getBlockState(blockPos2).isPathfindable(level, blockPos2, PathComputationType.LAND)) &&
                                ((level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getBrightness(LightLayer.SKY, blockPos2) == 0) ||
                                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.isNight()) ||
                                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.isThundering())))) {
                            glare.getBrain().setMemory(USMemoryModules.DARK_POS, blockPos2);
                            this.darkPos = blockPos2;
                            return;
                        }
                    }
                }
            }
        }
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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
        super.tick(level, entity, time);
        if (this.darkPos != null) {
            Brain<GlareEntity> brain = entity.getBrain();
            BlockPos groundPos = this.darkPos.below().below();
            if ((level.isInWorldBounds(darkPos) && level.getBlockState(darkPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isEmptyBlock(darkPos) && level.getBlockState(darkPos).isPathfindable(level, darkPos, PathComputationType.LAND) &&
                    ((level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.getBrightness(LightLayer.SKY, darkPos) == 0) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isNight()) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isThundering())))) {
                int i = brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN).get();
                if (brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN).isPresent()) {
                    boolean bl = this.pathfindDirectlyTowards(darkPos, entity);
                    if (bl) {
                        BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ());
                        BehaviorUtils.setWalkAndLookTargetMemories(entity, getNearbyPos(entity, blockPos), this.speed, 3);
                        if (entity.blockPosition().closerThan(darkPos, 3)) {
                            entity.setLightblock(blockPos);
                            entity.setGlowberries(i - 1);
                            this.darkPos = null;
                        }
                    } else {
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
        if (this.darkPos != null) {
            Brain<GlareEntity> brain = entity.getBrain();
            BlockPos entityPos = entity.blockPosition();
            BlockPos groundPos = this.darkPos.below().below();
            if ((level.isInWorldBounds(darkPos) && level.getBlockState(darkPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isEmptyBlock(darkPos) && level.getBlockState(darkPos).isPathfindable(level, darkPos, PathComputationType.LAND) &&
                    ((level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.getBrightness(LightLayer.SKY, darkPos) == 0) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isNight()) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isThundering())))) {
                int i = brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN).get();
                if (brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN).isPresent()) {
                    boolean bl = this.pathfindDirectlyTowards(darkPos, entity);
                    if (bl) {
                        BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ());
                        BehaviorUtils.setWalkAndLookTargetMemories(entity, getNearbyPos(entity, blockPos), this.speed, 3);
                        if (entity.blockPosition().closerThan(darkPos, 3)) {
                            entity.setLightblock(blockPos);
                            entity.setGlowberries(i - 1);
                            this.darkPos = null;
                        }
                    } else {
                        this.darkPos = null;
                    }
                }
            } else {
                this.darkPos = null;
            }
        }
    }

    @Override
    protected void start(ServerLevel level, GlareEntity entity, long time) {
        this.groundNavigation = new GroundPathNavigation(entity, level);
        this.getDarkPos(level, entity);
        if (this.darkPos != null) {
            Brain<GlareEntity> brain = entity.getBrain();
            BlockPos entityPos = entity.blockPosition();
            BlockPos groundPos = this.darkPos.below().below();
            if ((level.isInWorldBounds(darkPos) && level.getBlockState(darkPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isEmptyBlock(darkPos) && level.getBlockState(darkPos).isPathfindable(level, darkPos, PathComputationType.LAND) &&
                    ((level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.getBrightness(LightLayer.SKY, darkPos) == 0) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isNight()) ||
                            (level.getBrightness(LightLayer.BLOCK, darkPos) == 0 && level.isThundering())))) {
                int i = brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN).get();
                if (brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN).isPresent()) {
                    boolean bl = this.pathfindDirectlyTowards(darkPos, entity);
                    if (bl) {
                        BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ());
                        BehaviorUtils.setWalkAndLookTargetMemories(entity, getNearbyPos(entity, blockPos), this.speed, 3);
                        if (entity.blockPosition().closerThan(darkPos, 3)) {
                            entity.setLightblock(blockPos);
                            entity.setGlowberries(i - 1);
                            this.darkPos = null;
                        }
                    } else {
                        this.darkPos = null;
                    }
                }
            } else {
                this.darkPos = null;
            }
        }
    }
}