package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USActivities {

    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, UnvotedAndShelved.MODID);

    public static final RegistryObject<Activity> GOTO_DARKNESS = register("goto_darkness");

    public static RegistryObject<Activity> register(String key) {
        return ACTIVITIES.register(key, () -> new Activity(key));
    }

}
