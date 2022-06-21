package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.blocks.*;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

// Block Registry

public class USBlocks {

    public static final CoreRegistry<Block> BLOCKS = CoreRegistry.create(Registry.BLOCK_REGISTRY, UnvotedAndShelved.MODID);

    public static final Block GLOWBERRY_DUST = register("glowberry_dust", new GlowberryDustBlock(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0f, 3600000.8f).noLootTable().sound(USSounds.GLOW).lightLevel(GlowberryDustBlock.LIGHT_EMISSION)));
    public static final Block COPPER_BUTTON = register("copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block EXPOSED_COPPER_BUTTON = register("exposed_copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block WEATHERED_COPPER_BUTTON = register("weathered_copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block OXIDIZED_COPPER_BUTTON = register("oxidized_copper_button", new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_COPPER_BUTTON = register("waxed_copper_button", new CopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_EXPOSED_COPPER_BUTTON = register("waxed_exposed_copper_button", new CopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_WEATHERED_COPPER_BUTTON = register("waxed_weathered_copper_button", new CopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block WAXED_OXIDIZED_COPPER_BUTTON = register("waxed_oxidized_copper_button", new CopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final Block COPPER_PILLAR = register("copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block EXPOSED_COPPER_PILLAR = register("exposed_copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WEATHERED_COPPER_PILLAR = register("weathered_copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block OXIDIZED_COPPER_PILLAR = register("oxidized_copper_pillar", new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_COPPER_PILLAR = register("waxed_copper_pillar", new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_EXPOSED_COPPER_PILLAR = register("waxed_exposed_copper_pillar", new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_WEATHERED_COPPER_PILLAR = register("waxed_weathered_copper_pillar", new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final Block WAXED_OXIDIZED_COPPER_PILLAR = register("waxed_oxidized_copper_pillar", new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static Block register(String id, Block block) {
        return BLOCKS.register(id, block);
    }

    public static Block register(String id, Block block, CreativeModeTab tab) {
        Block registry = register(id, block);
        USItems.register(id, new BlockItem(registry, new Item.Properties().tab(tab)));
        return registry;
    }
}