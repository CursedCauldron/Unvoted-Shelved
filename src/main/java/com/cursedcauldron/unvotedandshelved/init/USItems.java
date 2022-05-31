package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.item.FrozenCopperGolemItem;
import com.cursedcauldron.unvotedandshelved.item.GlowberryDustBlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UnvotedAndShelved.MODID);

    public static final RegistryObject<Item> GLOWBERRY_DUST_BOTTLE = ITEMS.register("glowberry_dust_bottle", () -> new GlowberryDustBlockItem(USBlocks.GLOWBERRY_DUST.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GLARE_SPAWN_EGG = ITEMS.register("glare_spawn_egg", () -> new ForgeSpawnEggItem(USEntityTypes.GLARE, 7837492, 5204011, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> FROZEN_COPPER_GOLEM_ITEM = ITEMS.register("oxidized_copper_golem", () -> new FrozenCopperGolemItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_REDSTONE)));

}
