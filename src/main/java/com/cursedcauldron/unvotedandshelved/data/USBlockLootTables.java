package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

public class USBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        dropSelf(USBlocks.COPPER_BUTTON.get());
        dropSelf(USBlocks.EXPOSED_COPPER_BUTTON.get());
        dropSelf(USBlocks.WEATHERED_COPPER_BUTTON.get());
        dropSelf(USBlocks.OXIDIZED_COPPER_BUTTON.get());
        dropSelf(USBlocks.WAXED_COPPER_BUTTON.get());
        dropSelf(USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get());
        dropSelf(USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get());
        dropSelf(USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
        dropSelf(USBlocks.COPPER_PILLAR.get());
        dropSelf(USBlocks.EXPOSED_COPPER_PILLAR.get());
        dropSelf(USBlocks.WEATHERED_COPPER_PILLAR.get());
        dropSelf(USBlocks.OXIDIZED_COPPER_PILLAR.get());
        dropSelf(USBlocks.WAXED_COPPER_PILLAR.get());
        dropSelf(USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get());
        dropSelf(USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get());
        dropSelf(USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return USBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
}
