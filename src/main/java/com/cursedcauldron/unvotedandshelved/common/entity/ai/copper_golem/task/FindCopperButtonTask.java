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
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Objects;

public class FindCopperButtonTask extends Behavior<CopperGolemEntity> {
    private BlockPos copperPosPublic;
    private BlockPos copperPosBelowPublic;


    public FindCopperButtonTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.HURT_BY, MemoryStatus.VALUE_ABSENT, USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, USMemoryModules.COPPER_BUTTON, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, CopperGolemEntity entity) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON).isEmpty() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel world, CopperGolemEntity entity, long p_22547_) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON).isEmpty() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
    }



    @Override
    protected void start(ServerLevel level, CopperGolemEntity entity, long p_22542_) {
        BlockPos copperPos = this.getCopperPos(entity);
        if (copperPos != null) {
            this.copperPosPublic = copperPos;
            BlockPos copperPosBelow = this.getCopperPos(entity).below();
            if (copperPosBelow != null) {
                this.copperPosBelowPublic = copperPosBelow;
            }
        }
    }

    @Override
    protected void tick(ServerLevel level, CopperGolemEntity entity, long l) {
        if (this.copperPosPublic != null && this.copperPosBelowPublic != null) {
            BlockPos copperPos = this.copperPosPublic;
            BlockPos copperPosBelow = this.copperPosBelowPublic;
            BehaviorUtils.setWalkAndLookTargetMemories(entity, copperPos, 0.4F, 1);
            Path button = entity.getNavigation().createPath(copperPos, 1);
            Path buttonBelow = entity.getNavigation().createPath(copperPosBelow, 1);
            if (button != null) {
                if (button.canReach()) {
                    entity.getNavigation().moveTo(button, 0.4);
                    if (entity.blockPosition().closerThan(copperPos, 2) && entity.level.getBlockState(copperPos).getBlock() instanceof CopperButtonBlock) {
                        entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON, copperPos);
                        this.copperPosPublic = copperPos;
                        System.out.println("nice button");
                    }
                } else if (buttonBelow != null) {
                    if (buttonBelow.canReach()) {
                        entity.getNavigation().moveTo(button, 0.4);
                        if (entity.blockPosition().closerThan(copperPos, 2) && entity.level.getBlockState(copperPos).getBlock() instanceof CopperButtonBlock) {
                            entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON, copperPos);
                            this.copperPosPublic = copperPos;
                            System.out.println("nice button");
                        }
                    }
                }
            }
        }
    }

    public BlockPos getCopperPos(CopperGolemEntity entity) {
        int radius = 16;
        List<BlockPos> possibles = Lists.newArrayList();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -radius; y <= radius ; y++) {
                    BlockPos pos = new BlockPos(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                    if (entity.level.getBlockState(pos).getBlock() instanceof CopperButtonBlock) {
                        possibles.add(pos);
                    }
                }
            }
        }
        if (possibles.isEmpty()) {
            entity.setCooldown();
            return null;
        } else
        return possibles.get(entity.getRandom().nextInt(possibles.size()));
    }
}