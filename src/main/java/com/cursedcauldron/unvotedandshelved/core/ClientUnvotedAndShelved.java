package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;

//<>

@Environment(EnvType.CLIENT)
public class ClientUnvotedAndShelved implements ClientModInitializer {
    static {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(UnvotedAndShelved.GLOWBERRY_DUST_PARTICLES, FlameParticle.Provider::new);
    }

    @Override
    public void onInitializeClient() {
        USEntityRenderer.registerRenderers();
    }
}