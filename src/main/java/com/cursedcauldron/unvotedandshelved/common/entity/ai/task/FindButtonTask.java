package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Random;


public class FindButtonTask extends Task<CopperGolemEntity> {

    private final int range;
    private final float speed;

    public FindButtonTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_PRESENT));
        this.range = range;
        this.speed = speed;
    }

    private BlockPos getNearbyCopperButtons(CopperGolemEntity golem) {
        List<BlockPos> list = Lists.newArrayList();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos blockPos = new BlockPos(golem.getX() + x, golem.getY() + y, golem.getZ() + z);
                    if (golem.world.getBlockState(blockPos).isOf(USBlocks.COPPER_BUTTON)) {
                        list.add(blockPos);
                    }
                }
            }
        }
        if (list.isEmpty()) return null;

        return list.get(golem.getRandom().nextInt(list.size()));
    }

    private static int getRandomOffset(Random random) {
        return random.nextInt(3) - 1;
    }

    private static BlockPos getNearbyPos(CopperGolemEntity mob, BlockPos blockPos) {
        Random random = mob.world.random;
        return blockPos.add(getRandomOffset(random), 0, getRandomOffset(random));
    }

    @Override
    protected void run(ServerWorld level, CopperGolemEntity golem, long time) {
        BlockPos blockPos = getNearbyCopperButtons(golem);
        if (blockPos != null) {
            LookTargetUtil.walkTowards(golem, getNearbyPos(golem, blockPos), this.speed, 3);
            double distance = golem.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (distance <= range) {
                BlockState state = golem.world.getBlockState(blockPos);
                if (state.isOf(USBlocks.COPPER_BUTTON)) {
                    Direction direction = state.get(CopperButtonBlock.FACING);
                    golem.getLookControl().lookAt(blockPos.getX() + direction.getOffsetX(), blockPos.getY() + direction.getOffsetY(), blockPos.getZ() + direction.getOffsetZ());
                    if (golem.getCooldownTicks() == 0) {
                        ((AbstractButtonBlock) state.getBlock()).powerOn(state, golem.world, blockPos);
                    }
                }
            }
        }
    }

    @Override
    protected void finishRunning(ServerWorld level, CopperGolemEntity golem, long time) {
        BlockPos blockPos = getNearbyCopperButtons(golem);
        if (blockPos != null) {
            LookTargetUtil.walkTowards(golem, getNearbyPos(golem, blockPos), this.speed, 3);
            double distance = golem.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (distance <= range) {
                BlockState state = golem.world.getBlockState(blockPos);
                if (state.isOf(USBlocks.COPPER_BUTTON)) {
                    Direction direction = state.get(CopperButtonBlock.FACING);
                    golem.getLookControl().lookAt(blockPos.getX() + direction.getOffsetX(), blockPos.getY() + direction.getOffsetY(), blockPos.getZ() + direction.getOffsetZ());
                    if (golem.getCooldownTicks() == 0) {
                        ((AbstractButtonBlock) state.getBlock()).powerOn(state, golem.world, blockPos);
                    }
                }
            }
        }
    }

    @Override
    public void keepRunning(ServerWorld world, CopperGolemEntity golem, long time) {
        BlockPos blockPos = getNearbyCopperButtons(golem);
        if (blockPos != null) {
            LookTargetUtil.walkTowards(golem, getNearbyPos(golem, blockPos), this.speed, 3);
            double distance = golem.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (distance <= range) {
                BlockState state = golem.world.getBlockState(blockPos);
                if (state.isOf(USBlocks.COPPER_BUTTON)) {
                    Direction direction = state.get(CopperButtonBlock.FACING);
                    golem.getLookControl().lookAt(blockPos.getX() + direction.getOffsetX(), blockPos.getY() + direction.getOffsetY(), blockPos.getZ() + direction.getOffsetZ());
                    if (golem.getCooldownTicks() == 0) {
                        ((AbstractButtonBlock) state.getBlock()).powerOn(state, golem.world, blockPos);
                    }
                }
            }
        }
    }
}
