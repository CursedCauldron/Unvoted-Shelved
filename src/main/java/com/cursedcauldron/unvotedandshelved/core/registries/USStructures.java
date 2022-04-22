package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.world.gen.structures.RuinedCapitalStructure;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.mixin.StructureFeatureAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class USStructures {

    public static final StructureFeature<JigsawConfiguration> RUINED_CAPITAL = new RuinedCapitalStructure();

    public static void init() {
        StructureFeatureAccessor.callRegister(new ResourceLocation(UnvotedAndShelved.MODID, "ruined_capital").toString(), RUINED_CAPITAL, GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
    }

}
