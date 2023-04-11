package com.cursedcauldron.unvotedandshelved.events;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.entities.FrozenCopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.entities.GlareEntity;
import com.cursedcauldron.unvotedandshelved.init.USEntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(USEntityTypes.GLARE.get(), GlareEntity.createAttributes().build());
        event.put(USEntityTypes.COPPER_GOLEM.get(), CopperGolemEntity.createAttributes().build());
        event.put(USEntityTypes.FROZEN_COPPER_GOLEM.get(), FrozenCopperGolemEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void onLivingCreated(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Zombie zombie) {
            zombie.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, CopperGolemEntity.class, true));
        }
    }

}
