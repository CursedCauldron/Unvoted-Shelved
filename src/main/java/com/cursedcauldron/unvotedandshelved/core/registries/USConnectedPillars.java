package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.blocks.WeatheringRotatedPillarBlock;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class USConnectedPillars {
    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.create(Registry.BLOCK_REGISTRY, UnvotedAndShelved.MODID);


    public static final Block COPPER_PILLAR_CONNECTED_EXPOSED   = register("copper_pillar_connected_exposed", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block COPPER_PILLAR_CONNECTED_WEATHERED = register("copper_pillar_connected_weathered", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block COPPER_PILLAR_CONNECTED_OXIDIZED  = register("copper_pillar_connected_oxidized", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final Block EXPOSED_COPPER_PILLAR_CONNECTED_UNAFFECTED   = register("exposed_copper_pillar_connected_unaffected", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block EXPOSED_COPPER_PILLAR_CONNECTED_WEATHERED = register("exposed_copper_pillar_connected_weathered", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block EXPOSED_COPPER_PILLAR_CONNECTED_OXIDIZED  = register("exposed_copper_pillar_connected_oxidized", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final Block WEATHERED_COPPER_PILLAR_CONNECTED_EXPOSED   = register("weathered_copper_pillar_connected_exposed", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WEATHERED_COPPER_PILLAR_CONNECTED_UNAFFECTED = register("weathered_copper_pillar_connected_unaffected", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WEATHERED_COPPER_PILLAR_CONNECTED_OXIDIZED  = register("weathered_copper_pillar_connected_oxidized", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final Block OXIDIZED_COPPER_PILLAR_CONNECTED_EXPOSED   = register("oxidized_copper_pillar_connected_exposed", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block OXIDIZED_COPPER_PILLAR_CONNECTED_WEATHERED = register("oxidized_copper_pillar_connected_weathered", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block OXIDIZED_COPPER_PILLAR_CONNECTED_UNAFFECTED = register("oxidized_copper_pillar_connected_unaffected", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);


    public static Block register(String id, Block block) {
        return BLOCKS.register(id, block);
    }

    public static Block register(String id, Block block, CreativeModeTab tab) {
        Block registry = register(id, block);
        USItems.register(id, new BlockItem(registry, new Item.Properties().tab(tab)));
        return registry;
    }

}
