package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//<>

@Environment(EnvType.CLIENT)
public class ClientUnvotedAndShelved implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        USEntityRenderer.registerRenderers();
    }
}