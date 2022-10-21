package com.cursedcauldron.unvotedandshelved;

import com.cursedcauldron.unvotedandshelved.events.MiscEvents;
import com.cursedcauldron.unvotedandshelved.events.MobEvents;
import com.cursedcauldron.unvotedandshelved.init.USActivities;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USEntityTypes;
import com.cursedcauldron.unvotedandshelved.init.USFeatures;
import com.cursedcauldron.unvotedandshelved.init.USGameEvents;
import com.cursedcauldron.unvotedandshelved.init.USItems;
import com.cursedcauldron.unvotedandshelved.init.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.init.USParticleTypes;
import com.cursedcauldron.unvotedandshelved.init.USPoiTypes;
import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import com.cursedcauldron.unvotedandshelved.init.USStructureProcessors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UnvotedAndShelved.MODID)
public class UnvotedAndShelved {
    public static final String MODID = "unvotedandshelved";

    public UnvotedAndShelved() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(this::commonSetup);

        USActivities.ACTIVITIES.register(modEventBus);
        USBlocks.BLOCKS.register(modEventBus);
        USEntityTypes.ENTITY_TYPES.register(modEventBus);
        USFeatures.FEATURES.register(modEventBus);
        USItems.ITEMS.register(modEventBus);
        USMemoryModules.MEMORY_MODULES.register(modEventBus);
        USParticleTypes.PARTICLE_TYPES.register(modEventBus);
        USPoiTypes.POI_TYPES.register(modEventBus);
        USSoundEvents.SOUND_EVENTS.register(modEventBus);

        eventBus.register(this);
        eventBus.register(new MobEvents());
        eventBus.register(new MiscEvents());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            USStructureProcessors.init();
            USGameEvents.init();
        });
    }

}