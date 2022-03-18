package com.cursedcauldron.unvotedandshelved.entities.ai.copper_golem;

import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.schedule.Activity;

public class CopperGolemBrain {

    public static Brain<?> create(CopperGolemEntity copperGolemEntity, Brain<CopperGolemEntity> brain) {
        addCoreActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<CopperGolemEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0f), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS.get())));
    }

}
