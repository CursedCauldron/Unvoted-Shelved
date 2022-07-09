package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.mixin.access.PoiTypesAccessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class USPoiTypes {

    public static void init() {
    }

    private static final Set<BlockState> RODS = ImmutableList.of(
            USBlocks.EXPOSED_LIGHTNING_ROD,
            USBlocks.WEATHERED_LIGHTNING_ROD,
            USBlocks.OXIDIZED_LIGHTNING_ROD,
            USBlocks.WAXED_LIGHTNING_ROD,
            USBlocks.WAXED_EXPOSED_LIGHTNING_ROD,
            USBlocks.WAXED_WEATHERED_LIGHTNING_ROD,
            USBlocks.WAXED_OXIDIZED_LIGHTNING_ROD
    ).stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet());

    public static final ResourceKey<PoiType> LIGHTNING_RODS = createKey("lightning_rods", RODS);

    private static ResourceKey<PoiType> createKey(String key, Set<BlockState> states) {
        ResourceKey<PoiType> entry = ResourceKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, key));
        PoiTypesAccessor.callRegister(Registry.POINT_OF_INTEREST_TYPE, entry, states, 1, 1);
        return entry;
    }

}
