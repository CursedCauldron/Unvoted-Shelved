package com.cursedcauldron.unvotedandshelved.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class USPoiTypes {

    public static void init (){
    }

    public static final ResourceKey<PoiType> LIGHTNING_RODS = createKey("lightning_rods");

    private static ResourceKey<PoiType> createKey(String string) {
        return ResourceKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, string));
    }
}
