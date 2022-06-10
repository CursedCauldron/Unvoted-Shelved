package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.world.gen.features.CopperGolemFeature;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

// Feature Registry

public class USFeatures {

    public static final Feature<NoneFeatureConfiguration> COPPER_GOLEM = new CopperGolemFeature(NoneFeatureConfiguration.CODEC);

    public static void init() {
        Registry.register(Registry.FEATURE, new ResourceLocation(UnvotedAndShelved.MODID, "copper_golem"), COPPER_GOLEM);
    }

}
