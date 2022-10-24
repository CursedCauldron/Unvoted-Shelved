package com.cursedcauldron.unvotedandshelved.api;

import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;

import static dev.lambdaurora.lambdynlights.api.DynamicLightHandlers.registerDynamicLightHandler;

public class LambdDynLightsImpl implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights() {
        registerDynamicLightHandler(USEntities.GLARE, entity -> entity.getGlowberries() != 0 ? entity.getGlowberries() * 3 : 0);
    }
}
