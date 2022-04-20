package com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.EntityPoses;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.core.registries.USSounds;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.pathfinder.Path;

import java.util.Map;

public class SpinHead extends Behavior<CopperGolemEntity> {
    private int spinningTicks;

    public SpinHead() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, CopperGolemEntity entity) {
        return (entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty() && entity.getBrain().getMemory(USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS).isEmpty());
    }

    @Override
    protected boolean canStillUse(ServerLevel world, CopperGolemEntity entity, long p_22547_) {
        return (entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty() && entity.getBrain().getMemory(USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS).isEmpty());
    }

    @Override
    protected void tick(ServerLevel world, CopperGolemEntity entity, long p_22553_) {
        if (this.spinningTicks < 40) {
            this.spinningTicks++;
        } else {
            entity.getBrain().setMemory(USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS, UniformInt.of(120, 200).sample(world.getRandom()));
        }
        if (this.spinningTicks == 20) {
            entity.playSound(USSounds.HEAD_SPIN, 1, 1);
            entity.setPose(EntityPoses.HEAD_SPIN);
        }
    }

    @Override
    protected void start(ServerLevel level, CopperGolemEntity entity, long p_22542_) {
        entity.getNavigation().stop();
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void stop(ServerLevel serverLevel, CopperGolemEntity livingEntity, long l) {
        if (this.spinningTicks >= 1) {
            this.spinningTicks = 0;
            livingEntity.setPose(Pose.STANDING);
        }
    }
}
