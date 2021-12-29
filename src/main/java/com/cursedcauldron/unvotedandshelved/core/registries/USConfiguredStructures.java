package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.Maps;
import net.minecraft.structure.PlainsVillageData;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Map;

public class USConfiguredStructures {
    private static final Map<String, ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURES = Maps.newHashMap();

    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_RUINED_CAPITAL = addStructure("configured_ruined_capital", USStructures.RUINED_CAPITAL.configure(new StructurePoolFeatureConfig(() -> PlainsVillageData.STRUCTURE_POOLS, 0)));

    public static ConfiguredStructureFeature<?, ?> addStructure(String id, ConfiguredStructureFeature<?, ?> configuredStructureFeature) {
        CONFIGURED_STRUCTURES.put(id, configuredStructureFeature);
        return configuredStructureFeature;
    }

    public static void registerConfiguredStructures() {
        for (String id : CONFIGURED_STRUCTURES.keySet()) {
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, UnvotedAndShelved.ID(id), CONFIGURED_STRUCTURES.get(id));
        }
    }

}
