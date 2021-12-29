package com.cursedcauldron.unvotedandshelved.common.entity.ai.goals;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.EnumSet;
import java.util.List;

public class FindButtonGoal extends Goal {
    private final CopperGolemEntity golem;
    private int pressingTicks;

    public FindButtonGoal(CopperGolemEntity golem) {
        this.golem = golem;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.golem.getCooldownTicks() > 0 || this.golem.canPerformGoal()) return false;

        return this.golem.isAlive() && this.getNearbyCopperButtons() != null;
    }

    @Override
    public boolean shouldContinue() {
        return this.pressingTicks <= 200;
    }

    @Override
    public void start() {
        this.golem.setPerformGoal(true);
        this.golem.setCooldownTicks(500);
    }

    @Override
    public void stop() {
        this.pressingTicks = 0;
        this.golem.setPerformGoal(false);
    }

    @Override
    public void tick() {
        BlockPos blockPos = getNearbyCopperButtons();
        if (blockPos != null) {
            this.pressingTicks++;
            this.golem.getLookControl().lookAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.golem.getNavigation().startMovingTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
            double distance = this.golem.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (distance < 3.0D) {
                BlockState state = this.golem.world.getBlockState(blockPos);
                if (state.isOf(USBlocks.COPPER_BUTTON)) {
                    Direction direction = state.get(CopperButtonBlock.FACING);
                    this.golem.getLookControl().lookAt(blockPos.getX() + direction.getOffsetX(), blockPos.getY() + direction.getOffsetY(), blockPos.getZ() + direction.getOffsetZ());
                    if (this.pressingTicks % 6 == 0) {
                        ((AbstractButtonBlock) state.getBlock()).powerOn(state, this.golem.world, blockPos);
                    }
                }
            }
        }
    }

    private BlockPos getNearbyCopperButtons() {
        List<BlockPos> list = Lists.newArrayList();
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos blockPos = new BlockPos(this.golem.getX() + x, this.golem.getY() + y, this.golem.getZ() + z);
                    if (this.golem.world.getBlockState(blockPos).isOf(USBlocks.COPPER_BUTTON)) {
                        list.add(blockPos);
                    }
                }
            }
        }
        if (list.isEmpty()) return null;

        return list.get(this.golem.getRandom().nextInt(list.size()));
    }
}
