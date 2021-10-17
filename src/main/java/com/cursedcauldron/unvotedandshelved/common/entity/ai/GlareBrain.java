package com.cursedcauldron.unvotedandshelved.common.entity.ai;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.AerealStrollTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.SeekDarknessTask;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CompositeTask;
import net.minecraft.entity.ai.brain.task.ConditionalTask;
import net.minecraft.entity.ai.brain.task.FollowMobTask;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.TimeLimitedTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.brain.task.WanderAroundTask;
import net.minecraft.util.math.intprovider.UniformIntProvider;

//<>

public class GlareBrain {
    public static Brain<?> create(Brain<GlareEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addFindDarknessActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<GlareEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(
                new StayAboveWaterTask(0.8F),
                new LookAroundTask(45, 90),
                new WanderAroundTask()
        ));
    }

    private static void addIdleActivities(Brain<GlareEntity> brain) {
        brain.setTaskList(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new TimeLimitedTask<>(new FollowMobTask(EntityType.PLAYER, 6.0F), UniformIntProvider.create(30, 60))),
                        Pair.of(1, new SeekDarknessTask(20, 0.6F)),
                        Pair.of(2, new CompositeTask<>(
                                ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
                                ImmutableSet.of(),
                                CompositeTask.Order.ORDERED,
                                CompositeTask.RunMode.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new AerealStrollTask(0.6F), 2),
                                        Pair.of(new StrollTask(0.6F), 2),
                                        Pair.of(new ConditionalTask<>(GlareEntity::isInAir, new WaitTask(30, 60)), 5),
                                        Pair.of(new ConditionalTask<>(GlareEntity::isOnGround, new WaitTask(30, 60)), 5)
                                )))
                ));
    }

    public static void addFindDarknessActivities(Brain<GlareEntity> brain) {
//        brain.registerActivity(USActivity.FIND_DARKNESS.get(),
//                ImmutableList.of(Pair.of(0, new SeekDarknessTask(20, 0.6F))));
//                ImmutableList.of(Pair.of(0, new FindDarkSpotTask(0.6F))));
    }

    public static void updateActivities(GlareEntity glare) {
//        glare.getBrain().switchActivities(ImmutableList.of(USActivity.FIND_DARKNESS.get(), Activity.IDLE));
    }
}