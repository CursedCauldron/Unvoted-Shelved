package com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.EntityPoses;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.core.registries.USSounds;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class SpinHeadTask extends Behavior<CopperGolemEntity> {
    private int spinningTicks;

    public SpinHeadTask() {
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
        int timeLimit = 40 + (entity.getStage().getId() * 10);
        int playSoundFrame = 20 + (entity.getStage().getId() * 5);
        if (this.spinningTicks < timeLimit) {
            this.spinningTicks++;
        } else {
            entity.getBrain().setMemory(USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS, UniformInt.of(120, 200).sample(world.getRandom()));
        }
        if (this.spinningTicks == playSoundFrame) {
            SoundEvent soundEvent;
            switch (entity.getStage()) {
                case UNAFFECTED -> soundEvent = USSounds.HEAD_SPIN;
                case EXPOSED -> soundEvent = USSounds.HEAD_SPIN_SLOWER;
                default -> soundEvent = USSounds.HEAD_SPIN_SLOWEST;
            }
            entity.playSound(soundEvent, 1.0F, 1.0F);
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
