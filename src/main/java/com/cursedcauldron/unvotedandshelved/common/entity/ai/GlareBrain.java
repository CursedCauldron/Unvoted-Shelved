package com.cursedcauldron.unvotedandshelved.common.entity.ai;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.AerialStrollTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.SeekDarknessTask;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.FIND_DARKNESS;

//<>

public class GlareBrain {
    public static boolean isGlowBerry(GlareEntity glare, ItemStack stack) {
        return stack.is(Items.GLOW_BERRIES);
    }

    public static Brain<?> create(GlareEntity glare, Brain<GlareEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<GlareEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new Swim(0.8F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
        ));
    }

    public static InteractionResult playerInteract(GlareEntity glare, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (isGlowBerry(glare, itemStack)) {
            addFindDarknessActivities(glare.getBrain());
            glare.setDarkTicks(1800);
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.CONSUME;
    }

    public static void addIdleActivities(Brain<GlareEntity> brain) {
        brain.addActivity(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
                        Pair.of(2, new GateBehavior<>(
                                ImmutableMap.of(net.minecraft.world.entity.ai.memory.MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                ImmutableSet.of(),
                                GateBehavior.OrderPolicy.ORDERED,
                                GateBehavior.RunningPolicy.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new AerialStrollTask(0.6F), 2),
                                        Pair.of(new RandomStroll(0.6F), 2),
                                        Pair.of(new RunIf<>(GlareEntity::isFlying, new DoNothing(30, 60)), 5),
                                        Pair.of(new RunIf<>(GlareEntity::isOnGround, new DoNothing(30, 60)), 5)
                                )))
                ));
    }

    public static void addFindDarknessActivities(Brain<GlareEntity> brain) {
        brain.addActivity(FIND_DARKNESS,
                ImmutableList.of(
                    Pair.of(0, new SeekDarknessTask(20, 0.6F)),
                    Pair.of(2, new GateBehavior<>(
                            ImmutableMap.of(),
                            ImmutableSet.of(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING),
                            GateBehavior.OrderPolicy.ORDERED,
                            GateBehavior.RunningPolicy.TRY_ALL,
                            ImmutableList.of()
                    ))));

    }

    public static void updateActivities(GlareEntity glare) {
                glare.getBrain().setActiveActivityToFirstValid(ImmutableList.of(FIND_DARKNESS, Activity.IDLE));
    }
}