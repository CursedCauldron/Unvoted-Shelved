package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USPoiTypes {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, UnvotedAndShelved.MODID);

    public static final RegistryObject<PoiType> EXPOSED_LIGHTNING_ROD = POI_TYPES.register("exposed_lightning_rod", () -> new PoiType(getBlockStates(USBlocks.EXPOSED_LIGHTNING_ROD.get()), 1, 1));
    public static final RegistryObject<PoiType> WEATHERED_LIGHTNING_ROD = POI_TYPES.register("weathered_lightning_rod", () -> new PoiType(getBlockStates(USBlocks.WEATHERED_LIGHTNING_ROD.get()), 1, 1));
    public static final RegistryObject<PoiType> OXIDIZED_LIGHTNING_ROD = POI_TYPES.register("oxidized_lightning_rod", () -> new PoiType(getBlockStates(USBlocks.OXIDIZED_LIGHTNING_ROD.get()), 1, 1));
    public static final RegistryObject<PoiType> WAXED_LIGHTNING_ROD = POI_TYPES.register("waxed_lightning_rod", () -> new PoiType(getBlockStates(USBlocks.WAXED_LIGHTNING_ROD.get()), 1, 1));
    public static final RegistryObject<PoiType> WAXED_EXPOSED_LIGHTNING_ROD = POI_TYPES.register("waxed_exposed_lightning_rod", () -> new PoiType(getBlockStates(USBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get()), 1, 1));
    public static final RegistryObject<PoiType> WAXED_WEATHERED_LIGHTNING_ROD = POI_TYPES.register("waxed_weathered_lightning_rod", () -> new PoiType(getBlockStates(USBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get()), 1, 1));
    public static final RegistryObject<PoiType> WAXED_OXIDIZED_LIGHTNING_ROD = POI_TYPES.register("waxed_oxidized_lightning_rod", () -> new PoiType(getBlockStates(USBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get()), 1, 1));

    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

}
