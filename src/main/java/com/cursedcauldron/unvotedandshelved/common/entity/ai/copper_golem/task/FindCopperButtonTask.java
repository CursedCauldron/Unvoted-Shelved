package com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FindCopperButtonTask extends Behavior<CopperGolemEntity> {
    private BlockPos copperPosPublic;
    private BlockPos copperPosBelowPublic;

    public FindCopperButtonTask() {
        super(ImmutableMap.of(MemoryModuleType.HURT_BY, MemoryStatus.VALUE_ABSENT, USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, USMemoryModules.COPPER_BUTTON, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel world, CopperGolemEntity entity) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON).isEmpty() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel world, CopperGolemEntity entity, long p_22547_) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON).isEmpty() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull CopperGolemEntity entity, long p_22542_) {
        BlockPos copperPos = this.getCopperPos(entity, level);
        if (copperPos != null) {
            this.copperPosPublic = copperPos;
            BlockPos copperPosBelow = this.getCopperPos(entity, level).below();
            if (copperPosBelow != null) {
                this.copperPosBelowPublic = copperPosBelow;
            }
        }
    }

    // Lets the Copper Golem find and press a Copper Button if it can reach it

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull CopperGolemEntity entity, long l) {
        if (this.copperPosPublic != null && this.copperPosBelowPublic != null) {
            BlockPos copperPos = this.copperPosPublic;
            BlockPos copperPosBelow = this.copperPosBelowPublic;
            BehaviorUtils.setWalkAndLookTargetMemories(entity, copperPos, 0.4F, 1);
            Path button = entity.getNavigation().createPath(copperPos, 1);
            Path buttonBelow = entity.getNavigation().createPath(copperPosBelow, 1);
            if (button != null && button.canReach()) {
                    entity.getNavigation().moveTo(button, 0.4);
                    if (entity.blockPosition().closerThan(copperPos, 2) && entity.level.getBlockState(copperPos).getBlock() instanceof CopperButtonBlock) {
                        entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON, copperPos);
                        this.copperPosPublic = copperPos;
                }
            } else if (buttonBelow != null && buttonBelow.canReach()) {
                    entity.getNavigation().moveTo(button, 0.4);
                    if (entity.blockPosition().closerThan(copperPos, 2) && entity.level.getBlockState(copperPos).getBlock() instanceof CopperButtonBlock) {
                        entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON, copperPos);
                        this.copperPosPublic = copperPos;
                }
            } else {
                entity.getBrain().eraseMemory(USMemoryModules.COPPER_BUTTON);
                entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, UniformInt.of(120, 240).sample(level.getRandom()));
            }
        }
    }

    // Gets the position of a nearby Copper Button

    public BlockPos getCopperPos(CopperGolemEntity entity, ServerLevel level) {
        int radius = 16;
        List<BlockPos> possibles = Lists.newArrayList();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -radius; y <= radius; y++) {
                    BlockPos pos = new BlockPos(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                    if (entity.level.getBlockState(pos).getBlock() instanceof CopperButtonBlock) {
                        possibles.add(pos);
                    }
                }
            }
        }
        if (possibles.isEmpty()) {
            entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, UniformInt.of(120, 240).sample(level.getRandom()));
            return null;
        } else {
            return possibles.get(entity.getRandom().nextInt(possibles.size()));
        }
    }
}
