package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.init.USPoiTypeTags;
import com.cursedcauldron.unvotedandshelved.init.USPoiTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class USPoiTypeTagsProvider extends PoiTypeTagsProvider {

    public USPoiTypeTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, UnvotedAndShelved.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        PoiType poiType = ForgeRegistries.POI_TYPES.getValue(new ResourceLocation("lightning_rod"));
        this.tag(USPoiTypeTags.LIGHTNING_RODS).add(poiType, USPoiTypes.EXPOSED_LIGHTNING_ROD.get(), USPoiTypes.WEATHERED_LIGHTNING_ROD.get(), USPoiTypes.OXIDIZED_LIGHTNING_ROD.get(), USPoiTypes.WAXED_LIGHTNING_ROD.get(), USPoiTypes.WAXED_EXPOSED_LIGHTNING_ROD.get(), USPoiTypes.WAXED_WEATHERED_LIGHTNING_ROD.get(), USPoiTypes.WAXED_OXIDIZED_LIGHTNING_ROD.get());
    }
}
