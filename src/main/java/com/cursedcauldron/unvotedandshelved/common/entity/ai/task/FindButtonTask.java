package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.registries.RegistryHelper;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class FindButtonTask extends Task<CopperGolemEntity> {

    private final int range;
    private final float speed;
    private BlockPos buttonPos;

    public FindButtonTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_PRESENT));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld level, CopperGolemEntity golem, long time) {
        return (this.getNearbyCopperButtons(golem) != null);
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

    private boolean pathfindDirectlyTowards(BlockPos blockPos, CopperGolemEntity entity) {
        entity.getNavigation().startMovingTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
        return entity.getNavigation().getCurrentPath() != null && entity.getNavigation().getCurrentPath().reachesTarget();
    }

    @Override
    public void run(ServerWorld world, CopperGolemEntity golem, long time) {
        this.buttonPos = getNearbyCopperButtons(golem);
    }

    @Override
    public void keepRunning(ServerWorld world, CopperGolemEntity golem, long time) {
        if (this.buttonPos != null) {
            if (golem.getCooldownTicks() == 0) {
                boolean bl = pathfindDirectlyTowards(this.buttonPos, golem);
                if (bl) {
                    LookTargetUtil.walkTowards(golem, getNearbyPos(golem, this.buttonPos), this.speed, 2);
                    BlockPos pos = Objects.requireNonNull(golem.getNavigation().getCurrentPath()).getTarget();
                    if (Objects.requireNonNull(golem.getNavigation().getCurrentPath()).isFinished()) {
                        BlockState state = golem.world.getBlockState(pos);
                        if (state.isOf(USBlocks.COPPER_BUTTON)) {
                            WallMountLocation direction = state.get(CopperButtonBlock.FACE);
                            if (golem.getCooldownTicks() == 0) {
                                ((AbstractButtonBlock) state.getBlock()).powerOn(state, golem.world, this.buttonPos);
                                if (direction == WallMountLocation.FLOOR) {
                                    golem.setButtonDownTicks(30);
                                } else if (direction == WallMountLocation.CEILING) {
                                    golem.setButtonUpTicks(30);
                                } else {
                                    golem.setButtonTicks(30);
                                }
                                golem.playSound(SoundRegistry.COPPER_CLICK, 0.3f, 1.0f);
                                golem.setCooldownTicks(golem.getCooldownState());
                            }
                        }
                    }
                }
            }
        }
    }
}
