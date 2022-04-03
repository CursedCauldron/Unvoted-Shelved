package com.cursedcauldron.unvotedandshelved.entities.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.block.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.init.USActivities;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class PressCopperButtonTask extends Behavior<CopperGolemEntity> {
    private int pressingTicks;

    public PressCopperButtonTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT, USMemoryModules.COPPER_BUTTON.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, CopperGolemEntity entity) {
        return entity.getStage() != CopperGolemEntity.Stage.OXIDIZED && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get()).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel world, CopperGolemEntity entity, long p_22547_) {
        return entity.getStage() != CopperGolemEntity.Stage.OXIDIZED && this.pressingTicks < 200;
    }

    @Override
    protected void start(ServerLevel world, CopperGolemEntity entity, long p_22542_) {
        Optional<BlockPos> optional = entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON.get());
        optional.ifPresent(blockPos -> {
            BlockState state = entity.level.getBlockState(blockPos);
            boolean flag = entity.blockPosition().closerThan(blockPos, 2);
            if (this.pressingTicks < 200 && flag && state.getBlock() instanceof CopperButtonBlock buttonBlock) {
                buttonBlock.press(state, world, blockPos);
                entity.getLookControl().setLookAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                entity.playSound(USSoundEvents.COPPER_CLICK.get(), 1.0F, 1.0F);
            }
        });
    }

    @Override
    protected void tick(ServerLevel p_22551_, CopperGolemEntity p_22552_, long p_22553_) {
        if (this.pressingTicks < 200) {
            this.pressingTicks++;
        }
        System.out.println("The pressing ticks are " + this.pressingTicks);
    }

    @Override
    protected void stop(ServerLevel world, CopperGolemEntity entity, long p_22550_) {
        System.out.println("Task is stopping...");
        if (this.pressingTicks == 200) {
            this.pressingTicks = 0;
            entity.getBrain().eraseMemory(USMemoryModules.COPPER_BUTTON.get());
            entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get(), 6000);
        }
//        entity.getBrain().eraseMemory(USMemoryModules.COPPER_BUTTON.get());
//        entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get(), UniformInt.of(30, 60).sample(world.getRandom()));
    }
}
