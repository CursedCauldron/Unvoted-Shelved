package com.cursedcauldron.unvotedandshelved.common.entity.ai;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.FindButtonTask;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;


public class CopperGolemBrain {

    public static Brain<?> create(CopperGolemEntity golem, Brain<CopperGolemEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<CopperGolemEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(
                new StayAboveWaterTask(0.8F),
                new LookAroundTask(45, 90),
                new WanderAroundTask()
        ));
    }

    public static void addIdleActivities(Brain<CopperGolemEntity> brain) {
        brain.setTaskList(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new TimeLimitedTask<>(new FollowMobTask(EntityType.PLAYER, 6.0F), UniformIntProvider.create(5, 10))),
                        Pair.of(2, new CompositeTask<>(
                                ImmutableMap.of(net.minecraft.entity.ai.brain.MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
                                ImmutableSet.of(),
                                CompositeTask.Order.ORDERED,
                                CompositeTask.RunMode.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new StrollTask(0.6F), 2),
                                        Pair.of(new FindButtonTask(16, 0.6F), 2)
                                )))
                ));
    }

    public static void updateActivities(CopperGolemEntity golem) {
        Brain<CopperGolemEntity> brain = golem.getBrain();
        brain.resetPossibleActivities(ImmutableList.of(Activity.IDLE));
    }
}
