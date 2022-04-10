package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.mixin.access.ActivityAccessor;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.schedule.Activity;

//<>

public class USActivities {
    public static final CoreRegistry<Activity> ACTIVITIES = CoreRegistry.create(Registry.ACTIVITY_REGISTRY, UnvotedAndShelved.MODID);

    public static final Activity GOTO_DARKNESS    = register("goto_darkness");
    public static final Activity GOTO_BUTTON      = register("goto_button");

    public static Activity register(String id) {
        return ACTIVITIES.register(id, ActivityAccessor.createActivity(id));
    }
}