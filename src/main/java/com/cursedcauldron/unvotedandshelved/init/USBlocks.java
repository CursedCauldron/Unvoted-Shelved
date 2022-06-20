package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.block.ConnectedRotatedPillarBlock;
import com.cursedcauldron.unvotedandshelved.block.CopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.block.GlowberryDustBlock;
import com.cursedcauldron.unvotedandshelved.block.WeatheringCopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.block.WeatheringRotatedPillarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UnvotedAndShelved.MODID);

    public static final RegistryObject<Block> GLOWBERRY_DUST = registerNoTabBlock("glowberry_dust", () -> new GlowberryDustBlock(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0F, 3600000.8F).noCollission().noLootTable().sound(USSoundEvents.USBlockSoundGroup.GLOW).lightLevel(state -> 10)));
    public static final RegistryObject<Block> COPPER_BUTTON = registerBlock("copper_button", () -> new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> EXPOSED_COPPER_BUTTON = registerBlock("exposed_copper_button", () -> new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> WEATHERED_COPPER_BUTTON = registerBlock("weathered_copper_button", () -> new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_COPPER_BUTTON = registerBlock("oxidized_copper_button", () -> new WeatheringCopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> WAXED_COPPER_BUTTON = registerBlock("waxed_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BUTTON = registerBlock("waxed_exposed_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BUTTON = registerBlock("waxed_weathered_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BUTTON = registerBlock("waxed_oxidized_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).noCollission().strength(0.5F).requiresCorrectToolForDrops().sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> COPPER_PILLAR = registerBlock("copper_pillar", () -> new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> EXPOSED_COPPER_PILLAR = registerBlock("exposed_copper_pillar", () -> new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> WEATHERED_COPPER_PILLAR = registerBlock("weathered_copper_pillar", () -> new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> OXIDIZED_COPPER_PILLAR = registerBlock("oxidized_copper_pillar", () -> new WeatheringRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> WAXED_COPPER_PILLAR = registerBlock("waxed_copper_pillar", () -> new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_PILLAR = registerBlock("waxed_exposed_copper_pillar", () -> new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_PILLAR = registerBlock("waxed_weathered_copper_pillar", () -> new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_PILLAR = registerBlock("waxed_oxidized_copper_pillar", () -> new ConnectedRotatedPillarBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<? extends B> supplier, CreativeModeTab tab) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        USItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
        return block;
    }

    public static <B extends Block> RegistryObject<B> registerNoTabBlock(String name, Supplier<? extends B> supplier) {
        return BLOCKS.register(name, supplier);
    }

}
