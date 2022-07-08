package com.cursedcauldron.unvotedandshelved.events;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.client.entity.models.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.entity.models.FrozenCopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.entity.models.GlareModel;
import com.cursedcauldron.unvotedandshelved.client.entity.render.CopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.FrozenCopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.GlareRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.feature.TechnobladePigLayer;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USEntityTypes;
import com.cursedcauldron.unvotedandshelved.init.USModelLayers;
import com.cursedcauldron.unvotedandshelved.init.USParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(USBlocks.GLOWBERRY_DUST.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.COPPER_PILLAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.EXPOSED_COPPER_PILLAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.WEATHERED_COPPER_PILLAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.OXIDIZED_COPPER_PILLAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.WAXED_COPPER_PILLAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(USEntityTypes.GLARE.get(), GlareRenderer::new);
        event.registerEntityRenderer(USEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
        event.registerEntityRenderer(USEntityTypes.FROZEN_COPPER_GOLEM.get(), FrozenCopperGolemRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(USModelLayers.GLARE, GlareModel::getLayerDefinition);
        event.registerLayerDefinition(USModelLayers.COPPER_GOLEM, CopperGolemModel::getLayerDefinition);
        event.registerLayerDefinition(USModelLayers.FROZEN_COPPER_GOLEM, FrozenCopperGolemModel::getLayerDefinition);
    }

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        engine.register(USParticleTypes.GLOWBERRY_DUST_PARTICLES.get(), FlameParticle.Provider::new);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        PigRenderer pigRenderer = event.getRenderer(EntityType.PIG);
        if (pigRenderer == null) return;
        pigRenderer.addLayer(new TechnobladePigLayer<>(pigRenderer, new PigModel<>(event.getEntityModels().bakeLayer(ModelLayers.PIG))));
    }

}
