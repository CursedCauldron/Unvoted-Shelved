package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class USItemTags {

    public static void init() { }

    public static final TagKey<Item> COPPER_BUTTONS = create("copper_buttons");

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, string));
    }

}
