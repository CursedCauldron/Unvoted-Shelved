package com.cursedcauldron.unvotedandshelved.common.entity.ai;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.AerealStrollTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.SeekDarknessTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.StrollTask;
import com.cursedcauldron.unvotedandshelved.core.registries.USActivity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.DummyTask;
import net.minecraft.entity.ai.brain.task.LookAtEntityTask;
import net.minecraft.entity.ai.brain.task.LookTask;
import net.minecraft.entity.ai.brain.task.MultiTask;
import net.minecraft.entity.ai.brain.task.RunSometimesTask;
import net.minecraft.entity.ai.brain.task.SupplementedTask;
import net.minecraft.entity.ai.brain.task.SwimTask;
import net.minecraft.entity.ai.brain.task.WalkToTargetTask;
import net.minecraft.util.RangedInteger;

//<>

public class GlareTasks {
    public static Brain<?> create(Brain<GlareEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addFindDarknessActivities(brain);
        brain.setPersistentActivities(ImmutableSet.of(Activity.CORE));
        brain.setFallbackActivity(Activity.IDLE);
        brain.switchToFallbackActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<GlareEntity> brain) {
        brain.registerActivity(Activity.CORE, 0, ImmutableList.of(
                new SwimTask(0.8F),
                new LookTask(45, 90),
                new WalkToTargetTask()
        ));
    }

    private static void addIdleActivities(Brain<GlareEntity> brain) {
        brain.registerActivity(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new RunSometimesTask<>(new LookAtEntityTask(EntityType.PLAYER, 6.0F), RangedInteger.createRangedInteger(30, 60))),
                        Pair.of(1, new SeekDarknessTask(20, 0.6F)),
                        Pair.of(2, new MultiTask<>(
                                ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT),
                                ImmutableSet.of(),
                                MultiTask.Ordering.ORDERED,
                                MultiTask.RunType.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new AerealStrollTask(0.6F), 2),
                                        Pair.of(new StrollTask(0.6F), 2),
                                        Pair.of(new SupplementedTask<>(GlareEntity::isInAir, new DummyTask(30, 60)), 5),
                                        Pair.of(new SupplementedTask<>(GlareEntity::isOnGround, new DummyTask(30, 60)), 5)
                                        )))
                ));
    }

    public static void addFindDarknessActivities(Brain<GlareEntity> brain) {
        brain.registerActivity(USActivity.FIND_DARKNESS.get(),
                ImmutableList.of(Pair.of(0, new SeekDarknessTask(20, 0.6F))));
//                ImmutableList.of(Pair.of(0, new FindDarkSpotTask(0.6F))));
    }

    public static void updateActivities(GlareEntity glare) {
//        glare.getBrain().switchActivities(ImmutableList.of(USActivity.FIND_DARKNESS.get(), Activity.IDLE));
    }
}