package com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Objects;

public class FindCopperButtonTask extends Behavior<CopperGolemEntity> {

    public FindCopperButtonTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.HURT_BY, MemoryStatus.VALUE_ABSENT, USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, CopperGolemEntity entity) {
        return this.getCopperPos(entity) != null && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel world, CopperGolemEntity entity, long p_22547_) {
        boolean flag = entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
        return this.getCopperPos(entity) != null && flag;
    }

    protected void tick(ServerLevel world, CopperGolemEntity entity, long p_22553_) {
        if (!Objects.requireNonNull(entity.getNavigation().getPath()).canReach()) {
            entity.getBrain().eraseMemory(USMemoryModules.COPPER_BUTTON);
            entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, UniformInt.of(300, 600).sample(world.getRandom()));
        }
    }

    @Override
    protected void start(ServerLevel p_22540_, CopperGolemEntity entity, long p_22542_) {
        BlockPos copperPos = this.getCopperPos(entity);
        if (copperPos != null) {
            BehaviorUtils.setWalkAndLookTargetMemories(entity, copperPos, 0.6F, 1);
            if (entity.blockPosition().closerThan(copperPos, 2) && entity.level.getBlockState(copperPos).getBlock() instanceof CopperButtonBlock) {
                entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON, copperPos);
            }
        }
    }

    public BlockPos getCopperPos(CopperGolemEntity entity) {
        int radius = 8;
        List<BlockPos> possibles = Lists.newArrayList();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -1; y <= 1 ; y++) {
                    BlockPos pos = new BlockPos(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                    if (entity.level.getBlockState(pos).getBlock() instanceof CopperButtonBlock) {
                        possibles.add(pos);
                    }
                }
            }
        }
        if (possibles.isEmpty()) {
            entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, UniformInt.of(300, 600).sample(entity.getRandom()));
            return null;
        } else
        return possibles.get(entity.getRandom().nextInt(possibles.size()));
    }
}
