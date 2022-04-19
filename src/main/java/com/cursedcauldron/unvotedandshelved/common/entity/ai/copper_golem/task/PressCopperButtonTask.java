package com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.EntityPoses;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.core.registries.USSounds;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

import java.util.Optional;

public class PressCopperButtonTask extends Behavior<CopperGolemEntity> {
    private int buttonTicks;

    public PressCopperButtonTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT, USMemoryModules.COPPER_BUTTON, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, CopperGolemEntity entity) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON).isPresent() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel world, CopperGolemEntity entity, long p_22547_) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON).isPresent() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS).isEmpty();
    }

    @Override
    protected void start(ServerLevel world, CopperGolemEntity entity, long p_22542_) {
        Optional<BlockPos> optional = entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON);
        optional.ifPresent(blockPos -> {
            BlockState state = entity.level.getBlockState(blockPos);
            boolean flag = entity.blockPosition().closerThan(blockPos, 2);
            if (flag && state.getBlock() instanceof CopperButtonBlock buttonBlock) {
                AttachFace direction = state.getValue(CopperButtonBlock.FACE);
                entity.getLookControl().setLookAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                if (direction == AttachFace.FLOOR) {
                    entity.setPose(EntityPoses.PRESS_BUTTON_DOWN);
                    buttonBlock.press(state, world, blockPos);
                } else if (direction == AttachFace.CEILING) {
                    entity.setPose(EntityPoses.PRESS_BUTTON_UP);
                    buttonBlock.press(state, world, blockPos);
                } else {
                    entity.setPose(EntityPoses.PRESS_BUTTON);
                    buttonBlock.press(state, world, blockPos);
                }
                world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), USSounds.COPPER_CLICK, SoundSource.BLOCKS, 1, 1);
            }
        });
    }

    @Override
    protected void tick(ServerLevel world, CopperGolemEntity entity, long p_22553_) {
        if (this.buttonTicks < 60) {
            this.buttonTicks++;
        } else {
            entity.getBrain().eraseMemory(USMemoryModules.COPPER_BUTTON);
            entity.setCooldown();
            entity.setPose(Pose.STANDING);
        }
        System.out.println("The button ticks are " + this.buttonTicks);
    }


    @Override
    protected void stop(ServerLevel world, CopperGolemEntity entity, long p_22550_) {
        System.out.println("Task is stopping...");
        if (this.buttonTicks >= 1) {
            this.buttonTicks = 0;
            entity.setPose(Pose.STANDING);
        }
    }
}
