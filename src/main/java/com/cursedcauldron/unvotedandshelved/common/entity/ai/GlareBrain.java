package com.cursedcauldron.unvotedandshelved.common.entity.ai;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.AerialStrollTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.SeekDarknessTask;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.AxolotlBrain;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.FIND_DARKNESS;

//<>

public class GlareBrain {
    public static boolean isGlowBerry(GlareEntity glare, ItemStack stack) {
        return stack.isOf(Items.GLOW_BERRIES);
    }

    public static Brain<?> create(GlareEntity glare, Brain<GlareEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
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

    public static ActionResult playerInteract(GlareEntity glare, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (isGlowBerry(glare, itemStack)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        }
        return ActionResult.CONSUME;
    }

    private static void addIdleActivities(Brain<GlareEntity> brain) {
        brain.setTaskList(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new TimeLimitedTask<>(new FollowMobTask(EntityType.PLAYER, 6.0F), UniformIntProvider.create(30, 60))),
                        Pair.of(2, new CompositeTask<>(
                                ImmutableMap.of(net.minecraft.entity.ai.brain.MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
                                ImmutableSet.of(),
                                CompositeTask.Order.ORDERED,
                                CompositeTask.RunMode.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new AerialStrollTask(0.6F), 2),
                                        Pair.of(new StrollTask(0.6F), 2),
                                        Pair.of(new ConditionalTask<>(GlareEntity::isInAir, new WaitTask(30, 60)), 5),
                                        Pair.of(new ConditionalTask<>(GlareEntity::isOnGround, new WaitTask(30, 60)), 5)
                                )))
                ));
    }

    public static void addFindDarknessActivities(Brain<GlareEntity> brain) {
        brain.setTaskList(FIND_DARKNESS,
                ImmutableList.of(
                    Pair.of(0, new SeekDarknessTask(20, 0.6F)),
                    Pair.of(2, new CompositeTask<>(
                            ImmutableMap.of(),
                            ImmutableSet.of(UnvotedAndShelved.SEEK_TICKS),
                            CompositeTask.Order.ORDERED,
                            CompositeTask.RunMode.TRY_ALL,
                            ImmutableList.of()
                    ))));

    }

    public static void updateActivities(GlareEntity glare) {
                glare.getBrain().resetPossibleActivities(ImmutableList.of(FIND_DARKNESS, Activity.IDLE));
    }
}