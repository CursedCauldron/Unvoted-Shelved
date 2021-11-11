package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class ValidateSeekDarkness extends Behavior<GlareEntity> {
    public ValidateSeekDarkness() {
        super(ImmutableMap.of(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING, MemoryStatus.VALUE_PRESENT));
    }

    protected void start(ServerLevel serverLevel, GlareEntity glare, long l) {
        Brain<GlareEntity> brain = glare.getBrain();
        if (brain.getMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING).isPresent()) {
        int i = brain.getMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING).get();
        if (i <= 0) {
            System.out.println("Erasing memory");
            brain.eraseMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING);
            brain.eraseMemory(UnvotedAndShelved.GIVEN_GLOWBERRY);
            brain.useDefaultActivity();
        } else {
            System.out.println("Setting memory");
            brain.setMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING, (i - 1));
            }
        }
    }
}
