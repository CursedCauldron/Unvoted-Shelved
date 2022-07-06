package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USDataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        dataGenerator.addProvider(event.includeServer(), new USLootTableProvider(dataGenerator));
        dataGenerator.addProvider(event.includeServer(), new USRecipeProvider(dataGenerator));
        dataGenerator.addProvider(event.includeServer(), new USBlockTagsProvider(dataGenerator, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(), new USItemTagsProvider(dataGenerator, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(), new USGameEventTagsProvider(dataGenerator, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(), new USBiomeTagsProvider(dataGenerator, existingFileHelper));
    }

}
