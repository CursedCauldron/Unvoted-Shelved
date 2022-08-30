package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;

// Client Mod Initializer

@Environment(EnvType.CLIENT)
public class ClientUnvotedAndShelved implements ClientModInitializer {
    static {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(USParticles.GLOWBERRY_DUST_PARTICLES, FlameParticle.Provider::new);
    }

    @Override
    public void onInitializeClient() {

        // Registry

        USEntityRenderer.registerRenderers();
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.EXPOSED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WEATHERED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.OXIDIZED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_EXPOSED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_WEATHERED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_OXIDIZED_COPPER_PILLAR, RenderType.cutout());
//        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.BUTTERCUP, RenderType.cutout());
    }
}