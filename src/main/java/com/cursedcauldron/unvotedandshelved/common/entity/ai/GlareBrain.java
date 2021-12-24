package com.cursedcauldron.unvotedandshelved.common.entity.ai;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.AerialStrollTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.GlowberryStrollTask;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.CompositeTask;
import net.minecraft.entity.ai.brain.task.ConditionalTask;
import net.minecraft.entity.ai.brain.task.FollowMobTask;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.TimeLimitedTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.brain.task.WanderAroundTask;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.GLOWBERRIES_GIVEN;


//<>

public class GlareBrain {

    public static Brain<?> create(GlareEntity glare, Brain<GlareEntity> brain) {
        addCoreActivities(brain);
        addFindDarknessActivity(brain);
        addIdleActivities(brain);
        brain.remember(GLOWBERRIES_GIVEN, 0);
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

    private static Vec3d getRandomNearbyPos(GlareEntity glare) {
        Vec3d vec3 = FuzzyTargeting.find(glare, 4, 2);
        return vec3 == null ? glare.getPos() : vec3;
    }

    public static boolean isGlowBerry(GlareEntity glare, ItemStack stack) {
        return stack.isOf(Items.GLOW_BERRIES);
    }

    public static ActionResult playerInteract(GlareEntity glare, PlayerEntity player, Hand hand) {
        Brain<?> brain = glare.getBrain();
        ItemStack itemStack = player.getStackInHand(hand);
        if (brain.getOptionalMemory(GLOWBERRIES_GIVEN).isPresent()) {
            int i = brain.getOptionalMemory(GLOWBERRIES_GIVEN).get();
            if (!(i >= 5)) {
                if (isGlowBerry(glare, itemStack)) {
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        brain.remember(UnvotedAndShelved.GIVEN_GLOWBERRY, glare);
                    }
                    if (brain.getOptionalMemory(GLOWBERRIES_GIVEN).isPresent()) {
                        glare.setGlowberries(i + 1);
                        glare.playSound(SoundEvents.BLOCK_CAVE_VINES_PLACE, 1.0f, 1.0f);
                        return ActionResult.SUCCESS;
                    } else {
                        if (brain.getOptionalMemory(GLOWBERRIES_GIVEN).isPresent()) {
                            glare.setGlowberries(i + 1);
                            glare.playSound(SoundEvents.BLOCK_CAVE_VINES_PLACE, 1.0f, 1.0f);
                            return ActionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        glare.setPersistent();
        return ActionResult.CONSUME;
    }

    private static void addFindDarknessActivity(Brain<GlareEntity> brain) {
        brain.setTaskList(UnvotedAndShelved.GOTO_DARKNESS,
                ImmutableList.of(Pair.of(0, new GlowberryStrollTask(20, 0.6F))),
                ImmutableSet.of(Pair.of(UnvotedAndShelved.GIVEN_GLOWBERRY, MemoryModuleState.VALUE_PRESENT)),
                ImmutableSet.of(UnvotedAndShelved.GIVEN_GLOWBERRY));
    }


    public static void addIdleActivities(Brain<GlareEntity> brain) {
        brain.setTaskList(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new TimeLimitedTask<>(new FollowMobTask(EntityType.PLAYER, 6.0F), UniformIntProvider.create(5, 10))),
                        Pair.of(2, new CompositeTask<>(
                                ImmutableMap.of(net.minecraft.entity.ai.brain.MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
                                ImmutableSet.of(),
                                CompositeTask.Order.ORDERED,
                                CompositeTask.RunMode.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new AerialStrollTask(0.6F), 2),
                                        Pair.of(new StrollTask(0.6F), 2),
                                        Pair.of(new GlowberryStrollTask(10, 0.6F), 2),
                                        Pair.of(new ConditionalTask<>(GlareEntity::isInAir, new WaitTask(10, 20)), 5),
                                        Pair.of(new ConditionalTask<>(GlareEntity::isOnGround, new WaitTask(10, 20)), 5)
                )))
        ));
    }

    public static void updateActivities(GlareEntity glare) {
        Brain<GlareEntity> brain = glare.getBrain();
        brain.resetPossibleActivities(ImmutableList.of(UnvotedAndShelved.GOTO_DARKNESS, Activity.IDLE));
    }
}