package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.world.processors.RandomOxidationWallProcessor;
import com.cursedcauldron.unvotedandshelved.common.world.processors.RandomStoneReplacementProcessor;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class USStructureProcessors {

    public static void init() {
    }

    private static final Map<String, StructureProcessorType<?>> STRUCTURE_PROCESSOR_TYPE = Maps.newHashMap();

    public static final StructureProcessorType<RandomOxidationWallProcessor> OXIDATION_WALL_PROCESSOR = () -> RandomOxidationWallProcessor.CODEC;
    public static final StructureProcessorType<RandomStoneReplacementProcessor> STONE_WALL_PROCESSOR = () -> RandomStoneReplacementProcessor.CODEC;

    public static void initProcessors() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(UnvotedAndShelved.MODID, "oxidation_wall_processor"), OXIDATION_WALL_PROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(UnvotedAndShelved.MODID, "stone_wall_processor"), STONE_WALL_PROCESSOR);
    }

}
