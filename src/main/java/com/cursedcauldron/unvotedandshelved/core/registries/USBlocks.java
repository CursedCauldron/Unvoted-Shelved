package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.blocks.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlock;
import com.cursedcauldron.unvotedandshelved.common.blocks.WeatheringCopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.blocks.WeatheringRotatedPillarBlock;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

//<>

public class USBlocks {

    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.create(Registry.BLOCK_REGISTRY, UnvotedAndShelved.MODID);

    public static final Block GLOWBERRY_DUST                = register("glowberry_dust", new GlowberryDustBlock(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0f, 3600000.8f).noDrops().sound(USSounds.GLOW).lightLevel(GlowberryDustBlock.LIGHT_EMISSION)));
    public static final Block COPPER_BUTTON                 = register("copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block EXPOSED_COPPER_BUTTON         = register("exposed_copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block WEATHERED_COPPER_BUTTON       = register("weathered_copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block OXIDIZED_COPPER_BUTTON        = register("oxidized_copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_COPPER_BUTTON           = register("waxed_copper_button", new CopperButtonBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_EXPOSED_COPPER_BUTTON   = register("waxed_exposed_copper_button", new CopperButtonBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_WEATHERED_COPPER_BUTTON = register("waxed_weathered_copper_button", new CopperButtonBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_OXIDIZED_COPPER_BUTTON  = register("waxed_oxidized_copper_button", new CopperButtonBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_REDSTONE);
    public static final Block COPPER_PILLAR                 = register("copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block EXPOSED_COPPER_PILLAR         = register("exposed_copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WEATHERED_COPPER_PILLAR       = register("weathered_copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block OXIDIZED_COPPER_PILLAR        = register("oxidized_copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_COPPER_PILLAR           = register("waxed_copper_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_EXPOSED_COPPER_PILLAR   = register("waxed_exposed_copper_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_WEATHERED_COPPER_PILLAR = register("waxed_weathered_copper_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_OXIDIZED_COPPER_PILLAR  = register("waxed_oxidized_copper_pillar", new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), FabricLoader.getInstance().isModLoaded("modmenu") ? null : CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static Block register(String id, Block block) {
        return BLOCKS.register(id, block);
    }

    public static Block register(String id, Block block, CreativeModeTab tab) {
        Block registry = register(id, block);
        USItems.register(id, new BlockItem(registry, new Item.Properties().tab(tab)));
        return registry;
    }
}