package com.cursedcauldron.unvotedandshelved.common.entity.ai;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.AerialStrollTask;
import com.cursedcauldron.unvotedandshelved.common.entity.ai.task.GlowberryStrollTask;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;


//<>

public class GlareBrain {

    public static Brain<?> create(GlareEntity glare, Brain<GlareEntity> brain) {
        addCoreActivities(brain);
        addFindDarknessActivity(brain);
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
    private static Vec3 getRandomNearbyPos(GlareEntity glare) {
        Vec3 vec3 = LandRandomPos.getPos(glare, 4, 2);
        return vec3 == null ? glare.position() : vec3;
    }

    public static boolean isGlowBerry(GlareEntity glare, ItemStack stack) {
        return stack.is(Items.GLOW_BERRIES);
    }

    public static InteractionResult playerInteract(GlareEntity glare, Player player, InteractionHand hand) {
        Brain<?> brain = glare.getBrain();
        ItemStack itemStack = player.getItemInHand(hand);
        if (isGlowBerry(glare, itemStack)) {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
                brain.setMemory(UnvotedAndShelved.GIVEN_GLOWBERRY, glare);
                if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                    int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                    glare.setGlowberries(i + 1);
                    glare.playSound(SoundEvents.CAVE_VINES_PLACE, 1.0f, 1.0f);
                    return InteractionResult.SUCCESS;
                } else {
                    brain.setMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN, 0);
                    if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                        int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                        glare.setGlowberries(i + 1);
                        glare.playSound(SoundEvents.CAVE_VINES_PLACE, 1.0f, 1.0f);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                glare.setGlowberries(i + 1);
                glare.playSound(SoundEvents.CAVE_VINES_PLACE, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            } else {
                brain.setMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN, 0);
                if (brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).isPresent()) {
                    int i = brain.getMemory(UnvotedAndShelved.GLOWBERRIES_GIVEN).get();
                    glare.setGlowberries(i + 1);
                    glare.playSound(SoundEvents.CAVE_VINES_PLACE, 1.0f, 1.0f);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.CONSUME;
    }
    private static void addFindDarknessActivity(Brain<GlareEntity> brain) {
        brain.addActivityAndRemoveMemoriesWhenStopped(UnvotedAndShelved.GOTO_DARKNESS,
                ImmutableList.of(Pair.of(0, new GlowberryStrollTask(20,0.6F))),
                ImmutableSet.of(Pair.of(UnvotedAndShelved.GIVEN_GLOWBERRY, MemoryStatus.VALUE_PRESENT)),
                ImmutableSet.of(UnvotedAndShelved.GIVEN_GLOWBERRY));
    }


    public static void addIdleActivities(Brain<GlareEntity> brain) {
        brain.addActivity(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(5, 10))),
                        Pair.of(2, new GateBehavior<>(
                                ImmutableMap.of(net.minecraft.world.entity.ai.memory.MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                ImmutableSet.of(),
                                GateBehavior.OrderPolicy.ORDERED,
                                GateBehavior.RunningPolicy.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new AerialStrollTask(0.6F), 2),
                                        Pair.of(new RandomStroll(0.6F), 2),
                                        Pair.of(new GlowberryStrollTask(10,0.6F), 2),
                                        Pair.of(new RunIf<>(GlareEntity::isFlying, new DoNothing(10, 20)), 5),
                                        Pair.of(new RunIf<>(GlareEntity::isOnGround, new DoNothing(10, 20)), 5)
                                )))
                ));
    }

    public static void updateActivities(GlareEntity glare) {
        Brain<GlareEntity> brain = glare.getBrain();
        brain.setActiveActivityToFirstValid(ImmutableList.of(UnvotedAndShelved.GOTO_DARKNESS, Activity.IDLE));
    }
}