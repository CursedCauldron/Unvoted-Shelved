package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.init.USBlockTags;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class USBlockTagsProvider extends BlockTagsProvider {

    public USBlockTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, UnvotedAndShelved.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(USBlockTags.COPPER_BUTTONS).add(USBlocks.COPPER_BUTTON.get(), USBlocks.EXPOSED_COPPER_BUTTON.get(), USBlocks.WEATHERED_COPPER_BUTTON.get(), USBlocks.OXIDIZED_COPPER_BUTTON.get(), USBlocks.WAXED_COPPER_BUTTON.get(), USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(), USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(), USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
        this.tag(USBlockTags.COPPER_PILLARS).add(USBlocks.COPPER_PILLAR.get(), USBlocks.EXPOSED_COPPER_PILLAR.get(), USBlocks.WEATHERED_COPPER_PILLAR.get(), USBlocks.OXIDIZED_COPPER_PILLAR.get(), USBlocks.WAXED_COPPER_PILLAR.get(), USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get(), USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get(), USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get());
        this.tag(USBlockTags.LIGHTNING_ROD).add(Blocks.LIGHTNING_ROD, USBlocks.EXPOSED_LIGHTNING_ROD.get(), USBlocks.WEATHERED_LIGHTNING_ROD.get(), USBlocks.OXIDIZED_LIGHTNING_ROD.get(), USBlocks.WAXED_LIGHTNING_ROD.get(), USBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get(), USBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get(), USBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).addTags(USBlockTags.COPPER_BUTTONS, USBlockTags.COPPER_PILLARS, USBlockTags.LIGHTNING_ROD);
        this.tag(BlockTags.BUTTONS).addTag(USBlockTags.COPPER_BUTTONS);
    }
}
