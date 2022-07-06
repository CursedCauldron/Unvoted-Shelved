package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class USBlockTags {

    public static void init() {}

    public static final TagKey<Block> COPPER_BUTTONS = create("copper_buttons");
    public static final TagKey<Block> COPPER_PILLARS = create("copper_pillars");
    public static final TagKey<Block> LIGHTNING_ROD = create("lightning_rods");

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, string));
    }

}
