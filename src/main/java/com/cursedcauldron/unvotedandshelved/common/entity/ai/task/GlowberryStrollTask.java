package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import java.util.Random;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;


//<>

public class GlowberryStrollTask extends Task<GlareEntity> {
    private BlockPos darkPos;
    private final int range;
    private final float speed;
    protected MobNavigation groundNavigation;



    public GlowberryStrollTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_PRESENT));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean shouldRun(ServerWorld worldIn, GlareEntity owner) {
        return !owner.isInsideWaterOrBubbleColumn() && (owner.getBrain().getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get() >= 1);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld level, GlareEntity glare, long time) {
        return (glare.getBrain().getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get() >= 1) && (this.darkPos != null);
    }

    private boolean pathfindDirectlyTowards(BlockPos blockPos, GlareEntity entity) {
        entity.getNavigation().startMovingTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
        return entity.getNavigation().getCurrentPath() != null && entity.getNavigation().getCurrentPath().reachesTarget();
    }

    private boolean isValidSpawnPos(BlockPos blockPos, ServerWorld level) {
        return !level.getBlockState(blockPos).isOf(Blocks.VINE) && !level.getBlockState(blockPos).isIn(BlockTags.LEAVES) &&  !level.getBlockState(blockPos).isIn(BlockTags.CAVE_VINES) && !level.getBlockState(blockPos).isAir() && !level.getFluidState(blockPos).isOf(Fluids.WATER) && !level.getFluidState(blockPos).isOf(Fluids.FLOWING_WATER);
    }

    protected void getDarkPos(ServerWorld level, GlareEntity glare) {
        if (this.darkPos == null) {
            for (int x = getRandomNumber(0, -range); x <= getRandomNumber(0, range); x++) {
                for (int z = getRandomNumber(0, -range); z <= getRandomNumber(0, range); z++) {
                    for (int y = getRandomNumber(0, -range); y <= getRandomNumber(0, range); y++) {
                        BlockPos entityPos = glare.getBlockPos();
                        BlockPos blockPos2 = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                        BlockPos spacePos = blockPos2.down();
                        BlockPos groundPos = spacePos.down();
                        if ((level.isInBuildLimit(blockPos2) && isValidSpawnPos(groundPos, level) && level.isAir(spacePos) && level.isAir(blockPos2) && (level.getBlockState(blockPos2).canPathfindThrough(level, blockPos2, NavigationType.LAND)) &&
                                ((level.getLightLevel(LightType.BLOCK, blockPos2) == 0 && level.getLightLevel(LightType.SKY, blockPos2) == 0) ||
                                        (level.getLightLevel(LightType.BLOCK, blockPos2) == 0 && level.isNight()) ||
                                        (level.getLightLevel(LightType.BLOCK, blockPos2) == 0 && level.isThundering())))) {
                            glare.getBrain().remember(UnvotedAndShelved.DARK_POS, blockPos2);
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
        Random random = mob.world.random;
        return blockPos.add(getRandomOffset(random), 0, getRandomOffset(random));
    }

    @Override
    protected void keepRunning(ServerWorld level, GlareEntity entity, long time) {
        super.keepRunning(level, entity, time);
        if (this.darkPos != null) {
            Brain<GlareEntity> brain = entity.getBrain();
            BlockPos groundPos = this.darkPos.down().down();
            if ((level.isInBuildLimit(darkPos) && level.getBlockState(darkPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isAir(darkPos) && level.getBlockState(darkPos).canPathfindThrough(level, darkPos, NavigationType.LAND) &&
                    ((level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.getLightLevel(LightType.SKY, darkPos) == 0) ||
                            (level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.isNight()) ||
                            (level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.isThundering())))) {
                int i = brain.getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                if (brain.getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                    boolean bl = this.pathfindDirectlyTowards(darkPos, entity);
                    if (bl) {
                        BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ());
                        LookTargetUtil.walkTowards(entity, getNearbyPos(entity, blockPos), this.speed, 3);
                        if (entity.getBlockPos().isWithinDistance(darkPos, 3)) {
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
    protected void finishRunning(ServerWorld level, GlareEntity entity, long time) {
        if (this.darkPos != null) {
            Brain<GlareEntity> brain = entity.getBrain();
            BlockPos entityPos = entity.getBlockPos();
            BlockPos groundPos = this.darkPos.down().down();
            if ((level.isInBuildLimit(darkPos) && level.getBlockState(darkPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isAir(darkPos) && level.getBlockState(darkPos).canPathfindThrough(level, darkPos, NavigationType.LAND) &&
                    ((level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.getLightLevel(LightType.SKY, darkPos) == 0) ||
                            (level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.isNight()) ||
                            (level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.isThundering())))) {
                int i = brain.getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                if (brain.getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                    boolean bl = this.pathfindDirectlyTowards(darkPos, entity);
                    if (bl) {
                        BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ());
                        LookTargetUtil.walkTowards(entity, getNearbyPos(entity, blockPos), this.speed, 3);
                        if (entity.getBlockPos().isWithinDistance(darkPos, 3)) {
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
    protected void run(ServerWorld level, GlareEntity entity, long time) {
        this.groundNavigation = new MobNavigation(entity, level);
        this.getDarkPos(level, entity);
        if (this.darkPos != null) {
            Brain<GlareEntity> brain = entity.getBrain();
            BlockPos entityPos = entity.getBlockPos();
            BlockPos groundPos = this.darkPos.down().down();
            if ((level.isInBuildLimit(darkPos) && level.getBlockState(darkPos).isAir() && !level.getBlockState(groundPos).isAir() && level.isAir(darkPos) && level.getBlockState(darkPos).canPathfindThrough(level, darkPos, NavigationType.LAND) &&
                    ((level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.getLightLevel(LightType.SKY, darkPos) == 0) ||
                            (level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.isNight()) ||
                            (level.getLightLevel(LightType.BLOCK, darkPos) == 0 && level.isThundering())))) {
                int i = brain.getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                if (brain.getOptionalMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                    boolean bl = this.pathfindDirectlyTowards(darkPos, entity);
                    if (bl) {
                        BlockPos blockPos = new BlockPos(this.darkPos.getX(), this.darkPos.getY(), this.darkPos.getZ());
                        LookTargetUtil.walkTowards(entity, getNearbyPos(entity, blockPos), this.speed, 3);
                        if (entity.getBlockPos().isWithinDistance(darkPos, 3)) {
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