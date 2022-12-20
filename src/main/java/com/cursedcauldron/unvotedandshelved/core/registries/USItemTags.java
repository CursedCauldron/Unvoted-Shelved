package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class USItemTags {

    public static final TagKey<Item> MOOBLOOM_TEMPTATIONS = create("moobloom_temptations");

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(UnvotedAndShelved.MODID, string));
    }

}
