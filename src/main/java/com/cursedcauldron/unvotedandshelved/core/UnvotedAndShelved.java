package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.mixin.ActivityInvoker;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.ai.brain.Activity;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";
    public static Activity FIND_DARKNESS = ActivityInvoker.invokeRegister("find_darkness");

    @Override
    public void onInitialize() {
        Reflection.initialize(
                USEntities.class
        );

        FabricDefaultAttributeRegistry.register(USEntities.GLARE, GlareEntity.createGlareAttributes());
    }
}