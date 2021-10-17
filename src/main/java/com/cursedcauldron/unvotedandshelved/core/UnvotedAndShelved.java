package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";

    @Override
    public void onInitialize() {
        Reflection.initialize(
                USEntities.class
        );

        FabricDefaultAttributeRegistry.register(USEntities.GLARE, GlareEntity.createGlareAttributes());
    }
}