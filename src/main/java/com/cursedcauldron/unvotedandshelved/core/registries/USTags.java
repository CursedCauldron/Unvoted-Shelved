package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

// Tag Registry

public class USTags {

    public static void init() {
    }

    public static final TagKey<Block> COPPER_BUTTONS = create("copper_buttons");
    public static final TagKey<Block> COPPER_PILLARS = create("copper_pillars");

    private USTags() {
    }

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, string));
    }
}
