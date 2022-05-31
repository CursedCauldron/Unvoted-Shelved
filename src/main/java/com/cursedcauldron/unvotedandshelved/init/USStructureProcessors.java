package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.world.processors.CloseOffFluidSourcesProcessor;
import com.cursedcauldron.unvotedandshelved.world.processors.RandomOxidationWallProcessor;
import com.cursedcauldron.unvotedandshelved.world.processors.RandomStoneReplacementProcessor;
import com.cursedcauldron.unvotedandshelved.world.processors.WaterloggedProcessor;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class USStructureProcessors {

    public static final Map<StructureProcessorType<?>, ResourceLocation> STRUCTURE_PROCESSORS = Maps.newLinkedHashMap();

    public static final StructureProcessorType<RandomOxidationWallProcessor> OXIDATION_WALL_PROCESSOR = register("oxidation_wall_processor", () -> RandomOxidationWallProcessor.CODEC);
    public static final StructureProcessorType<RandomStoneReplacementProcessor> STONE_WALL_PROCESSOR = register("stone_wall_processor", () -> RandomStoneReplacementProcessor.CODEC);
    public static final StructureProcessorType<WaterloggedProcessor> WATERLOGGED_PROCESSOR = register("waterlogged_processor", () -> WaterloggedProcessor.CODEC);
    public static final StructureProcessorType<CloseOffFluidSourcesProcessor> CLOSE_OFF_FLUID_SOURCES_PROCESSOR = register("close_off_fluid_sources_processor", () -> CloseOffFluidSourcesProcessor.CODEC);

    public static <P extends StructureProcessor> StructureProcessorType<P> register(String name, StructureProcessorType<P> processorType) {
        STRUCTURE_PROCESSORS.put(processorType, new ResourceLocation(UnvotedAndShelved.MODID, name));
        return processorType;
    }

    public static void init() {
        for (StructureProcessorType<?> processorType : STRUCTURE_PROCESSORS.keySet()) {
            Registry.register(Registry.STRUCTURE_PROCESSOR, STRUCTURE_PROCESSORS.get(processorType), processorType);
        }
    }

}
