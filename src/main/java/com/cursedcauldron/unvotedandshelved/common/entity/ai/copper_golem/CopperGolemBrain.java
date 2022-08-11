package com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task.FindCopperButtonTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task.PressCopperButtonTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.copper_golem.task.SpinHeadTask;
import com.cursedcauldron.unvotedandshelved.core.registries.USActivities;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

/**
 * @author 0rc1nus
 */
public class CopperGolemBrain {

    public static Brain<?> create(Brain<CopperGolemEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addHeadSpinActivity(brain);
        addCopperButtonActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<CopperGolemEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new AnimalPanic(0.45f),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS)));
    }

    private static void addIdleActivities(Brain<CopperGolemEntity> brain) {
        brain.addActivityWithConditions(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
                        Pair.of(1, new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new RandomStroll(0.4F), 2),
                                        Pair.of(new SetWalkTargetFromLookTarget(0.4F, 3), 2),
                                        Pair.of(new DoNothing(30, 60), 1))
                        ))),
                ImmutableSet.of(
                        Pair.of(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT)));
    }

    private static void addCopperButtonActivities(Brain<CopperGolemEntity> brain) {
        brain.addActivityWithConditions(
                USActivities.PRESS_COPPER_BUTTON,
                ImmutableList.of(
                        Pair.of(0, new FindCopperButtonTask()),
                        Pair.of(1, new PressCopperButtonTask())
                ),
                ImmutableSet.of(
                        Pair.of(USMemoryModules.COPPER_BUTTON_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT
                ))
        );
    }

    private static void addHeadSpinActivity(Brain<CopperGolemEntity> brain) {
        brain.addActivityWithConditions(
                USActivities.HEAD_SPIN,
                ImmutableList.of(
                        Pair.of(0, new SpinHeadTask())
                ),
                ImmutableSet.of(
                        Pair.of(USMemoryModules.COPPER_GOLEM_HEADSPIN_TICKS, MemoryStatus.VALUE_ABSENT
                        ))
        );
    }

    public static void updateActivity(CopperGolemEntity entity) {
        entity.getBrain().setActiveActivityToFirstValid(
                ImmutableList.of(
                        USActivities.PRESS_COPPER_BUTTON, USActivities.HEAD_SPIN, Activity.IDLE
                )
        );
    }
}
