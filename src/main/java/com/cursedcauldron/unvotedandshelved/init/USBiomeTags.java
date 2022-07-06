package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class USBiomeTags {

    public static void init() {

    }

    public static final TagKey<Biome> RUINED_CAPITAL = create("has_structure/ruined_capital");

    private static TagKey<Biome> create(String string) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, string));
    }

}
