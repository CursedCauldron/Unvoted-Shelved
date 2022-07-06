package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.init.USBiomeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class USBiomeTagsProvider extends BiomeTagsProvider {

    public USBiomeTagsProvider(DataGenerator p_211094_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_211094_, UnvotedAndShelved.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(USBiomeTags.RUINED_CAPITAL).addTags(BiomeTags.IS_BADLANDS, BiomeTags.IS_RIVER, BiomeTags.IS_BEACH, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL, BiomeTags.IS_TAIGA, BiomeTags.IS_JUNGLE, BiomeTags.IS_FOREST).add(Biomes.STONY_PEAKS, Biomes.MUSHROOM_FIELDS, Biomes.ICE_SPIKES, Biomes.WINDSWEPT_SAVANNA, Biomes.SAVANNA, Biomes.DESERT, Biomes.SNOWY_PLAINS, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.SAVANNA_PLATEAU, Biomes.DRIPSTONE_CAVES, Biomes.LUSH_CAVES);
    }

}
