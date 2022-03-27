package com.cursedcauldron.unvotedandshelved.events;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.client.entity.models.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.entity.models.GlareModel;
import com.cursedcauldron.unvotedandshelved.client.entity.render.CopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.GlareRenderer;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USEntityTypes;
import com.cursedcauldron.unvotedandshelved.init.USModelLayers;
import com.cursedcauldron.unvotedandshelved.init.USParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(USBlocks.GLOWBERRY_DUST.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(USEntityTypes.GLARE.get(), GlareRenderer::new);
        event.registerEntityRenderer(USEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(USModelLayers.GLARE, GlareModel::getLayerDefinition);
        event.registerLayerDefinition(USModelLayers.COPPER_GOLEM, CopperGolemModel::getLayerDefinition);
    }

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        engine.register(USParticleTypes.GLOWBERRY_DUST_PARTICLES.get(), FlameParticle.Provider::new);
    }

}
