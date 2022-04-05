package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;


public class FindButtonTask extends Behavior<CopperGolemEntity> {

    private final int range;
    private final float speed;
    private BlockPos buttonPos;

    public FindButtonTask(int range, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, CopperGolemEntity golem, long time) {
        return golem.getStage() != CopperGolemEntity.Stage.OXIDIZED && (this.getNearbyCopperButtons(golem) != null);
    }

    private BlockPos getNearbyCopperButtons(CopperGolemEntity golem) {
        List<BlockPos> list = Lists.newArrayList();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    BlockPos blockPos = new BlockPos(golem.getX() + x, golem.getY() + y, golem.getZ() + z);
                    if (golem.level.getBlockState(blockPos).is(USBlocks.COPPER_BUTTON)) {
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
        Random random = mob.level.random;
        return blockPos.offset(getRandomOffset(random), 0, getRandomOffset(random));
    }

    private boolean pathfindDirectlyTowards(BlockPos blockPos, CopperGolemEntity entity) {
        entity.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
        return entity.getNavigation().getPath() != null && entity.getNavigation().getPath().canReach();
    }

    @Override
    public void start(ServerLevel world, CopperGolemEntity golem, long time) {
        this.buttonPos = getNearbyCopperButtons(golem);
    }

    @Override
    public void tick(ServerLevel world, CopperGolemEntity golem, long time) {
        if (this.buttonPos != null) {
            if (golem.getCooldownTicks() == 0) {
                boolean bl = pathfindDirectlyTowards(this.buttonPos, golem);
                if (bl) {
                    BehaviorUtils.setWalkAndLookTargetMemories(golem, getNearbyPos(golem, this.buttonPos), this.speed, 2);
                    BlockPos pos = Objects.requireNonNull(golem.getNavigation().getPath()).getTarget();
                    if (Objects.requireNonNull(golem.getNavigation().getPath()).isDone()) {
                        BlockState state = golem.level.getBlockState(pos);
                        if (state.is(USBlocks.COPPER_BUTTON)) {
                            AttachFace direction = state.getValue(CopperButtonBlock.FACE);
                            if (golem.getTicksFrozen() == 0) {
                                ((ButtonBlock) state.getBlock()).press(state, golem.level, this.buttonPos);
                                if (direction == AttachFace.FLOOR) {
                                    golem.setButtonDownTicks(30);
                                } else if (direction == AttachFace.CEILING) {
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
