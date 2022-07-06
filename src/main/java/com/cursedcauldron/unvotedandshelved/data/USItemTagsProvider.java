package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class USItemTagsProvider extends ItemTagsProvider {

    public USItemTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, new USBlockTagsProvider(dataGenerator, existingFileHelper), UnvotedAndShelved.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(USItemTags.COPPER_BUTTONS).add(USBlocks.COPPER_BUTTON.get().asItem(), USBlocks.EXPOSED_COPPER_BUTTON.get().asItem(), USBlocks.WEATHERED_COPPER_BUTTON.get().asItem(), USBlocks.OXIDIZED_COPPER_BUTTON.get().asItem(), USBlocks.WAXED_COPPER_BUTTON.get().asItem(), USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get().asItem(), USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get().asItem(), USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get().asItem());
        this.tag(ItemTags.BUTTONS).addTag(USItemTags.COPPER_BUTTONS);
    }
}
