package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//<>

public class USActivity {
    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, UnvotedAndShelved.MODID);

    public static final RegistryObject<Activity> FIND_DARKNESS = register("find_darkness");

    public static RegistryObject<Activity> register(String key) {
        return ACTIVITIES.register(key, () -> new Activity(key));
    }
}
