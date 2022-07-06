package com.cursedcauldron.unvotedandshelved.entities.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.block.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.init.USPoses;
import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

import java.util.Optional;

public class PressCopperButtonTask extends Behavior<CopperGolemEntity> {
    private int buttonTicks;

    public PressCopperButtonTask() {
        super(ImmutableMap.of(USMemoryModules.COPPER_BUTTON.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, CopperGolemEntity entity) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON.get()).isPresent() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get()).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel world, CopperGolemEntity entity, long p_22547_) {
        return entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON.get()).isPresent() && entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get()).isEmpty();
    }

    @Override
    protected void start(ServerLevel world, CopperGolemEntity entity, long p_22542_) {
        Optional<BlockPos> optional = entity.getBrain().getMemory(USMemoryModules.COPPER_BUTTON.get());
        optional.ifPresent(blockPos -> {
            BlockState state = entity.level.getBlockState(blockPos);
            boolean flag = entity.blockPosition().closerThan(blockPos, 2);
            if (flag && state.getBlock() instanceof CopperButtonBlock buttonBlock) {
                AttachFace direction = state.getValue(CopperButtonBlock.FACE);
                entity.getLookControl().setLookAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                Pose pose;
                switch (direction) {
                    case FLOOR -> pose = USPoses.PRESS_BUTTON_DOWN.get();
                    case CEILING -> pose = USPoses.PRESS_BUTTON_UP.get();
                    default -> pose = USPoses.PRESS_BUTTON.get();
                }
                entity.setPose(pose);
                buttonBlock.press(state, world, blockPos);
                world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), USSoundEvents.COPPER_CLICK.get(), SoundSource.BLOCKS, 1, 1);
            }
        });
    }

    @Override
    protected void tick(ServerLevel level, CopperGolemEntity entity, long p_22553_) {
        if (this.buttonTicks < 60) {
            this.buttonTicks++;
        } else {
            entity.getBrain().eraseMemory(USMemoryModules.COPPER_BUTTON.get());
            entity.getBrain().setMemory(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get(), UniformInt.of(120, 240).sample(level.getRandom()));
            entity.setPose(Pose.STANDING);
        }
    }

    @Override
    protected void stop(ServerLevel world, CopperGolemEntity entity, long p_22550_) {
        if (this.buttonTicks >= 1) {
            this.buttonTicks = 0;
            entity.setPose(Pose.STANDING);
        }
    }
}
