package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.entity.render.GlareRenderer;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USActivity;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//<>

@Mod(UnvotedAndShelved.MODID)
public class UnvotedAndShelved {
    public static final String MODID = "unvotedandshelved";
    public static final Logger LOGGER = LogManager.getLogger();

    public UnvotedAndShelved() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        this.registrySetup(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        GlobalEntityTypeAttributes.put(USEntities.GLARE.get(), GlareEntity.createAttributes().create());
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(USEntities.GLARE.get(), GlareRenderer::new);
    }

    private void registrySetup(final IEventBus bus) {
        USEntities.ENTITIES.register(bus);
        USActivity.ACTIVITIES.register(bus);
    }
}