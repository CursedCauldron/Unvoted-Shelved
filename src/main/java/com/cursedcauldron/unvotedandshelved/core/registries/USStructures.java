package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.world.structures.RuinedCapitalStructure;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class USStructures {

    public static final StructureFeature<StructurePoolFeatureConfig> RUINED_CAPITAL = new RuinedCapitalStructure(StructurePoolFeatureConfig.CODEC);

    public static void setupAndRegisterStructureFeatures() {

        FabricStructureBuilder.create(new Identifier(UnvotedAndShelved.MODID, "ruined_capital"), RUINED_CAPITAL)

                .step(GenerationStep.Feature.SURFACE_STRUCTURES)

                .defaultConfig(new StructureConfig(
                        10,
                        5,
                        399117345
                ))

                .adjustsSurface()

                .register();

    }

}
