package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

//<>

public class USPoiTags {
    public static void init() {}

    public static final TagKey<PoiType> LIGHTNING_RODS = create("lightning_rods");

    private static TagKey<PoiType> create(String key) {
        return TagKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, key));
    }
}