package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlockItem;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

//<>

public class USItems {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.create(Registry.ITEM_REGISTRY, UnvotedAndShelved.MODID);

    public static final Item GLARE_SPAWN_EGG        = register("glare_spawn_egg", new SpawnEggItem(USEntities.GLARE, 7837492, 5204011, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final Item GLOWBERRY_DUST_BOTTLE  = register("glowberry_dust_bottle", new GlowberryDustBlockItem(USBlocks.GLOWBERRY_DUST, new FabricItemSettings().tab(CreativeModeTab.TAB_DECORATIONS)));


    public static Item register(String id, Item item) {
        return ITEMS.register(id, item);
    }
}