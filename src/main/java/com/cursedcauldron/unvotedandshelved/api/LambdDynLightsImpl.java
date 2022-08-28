package com.cursedcauldron.unvotedandshelved.api;

import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import net.minecraft.world.entity.EntityType;

import static dev.lambdaurora.lambdynlights.api.DynamicLightHandlers.registerDynamicLightHandler;

public class LambdDynLightsImpl implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights() {
        registerDynamicLightHandler(USEntities.GLARE, entity -> {
            int luminance = 0;
            if (entity.getGlowberries() != 0)
                luminance = entity.getGlowberries() * 3;
            return luminance;
        });
    }
}
