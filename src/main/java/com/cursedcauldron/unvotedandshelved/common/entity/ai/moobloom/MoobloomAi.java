package com.cursedcauldron.unvotedandshelved.common.entity.ai.moobloom;

import com.cursedcauldron.unvotedandshelved.common.entity.Moobloom;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.crafting.Ingredient;

public class MoobloomAi {
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);

    public static Brain<?> makeBrain(Brain<Moobloom> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<Moobloom> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new Swim(0.8f),
                new AnimalPanic(2.0f),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
                )
        );
    }

    private static void initIdleActivity(Brain<Moobloom> brain) {
        brain.addActivity(Activity.IDLE, ImmutableList.of(
                Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0f), UniformInt.of(30, 60))),
                Pair.of(0, new AnimalMakeLove(USEntities.MOOBLOOM, 1.0f)),
                Pair.of(1, new FollowTemptation(livingEntity -> 1.25f)),
                Pair.of(2, new BabyFollowAdult<>(ADULT_FOLLOW_RANGE, 1.25f)),
                Pair.of(3, new RunOne<>(
                        ImmutableList.of(
                                Pair.of(new RandomStroll(1.0f), 2),
                                Pair.of(new SetWalkTargetFromLookTarget(1.0f, 3), 2),
                                Pair.of(new DoNothing(30, 60), 1))
                ))
        ));
    }

    public static Ingredient getTemptations() {
        return Moobloom.TEMPTATION_ITEM;
    }

    public static void updateActivity(Moobloom moobloom) {
        moobloom.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }

}
