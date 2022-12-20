package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.world.processors.CloseOffFluidSourcesProcessor;
import com.cursedcauldron.unvotedandshelved.common.world.processors.CopperPillarProcessor;
import com.cursedcauldron.unvotedandshelved.common.world.processors.RandomOxidationWallProcessor;
import com.cursedcauldron.unvotedandshelved.common.world.processors.RandomStoneReplacementProcessor;
import com.cursedcauldron.unvotedandshelved.common.world.processors.WaterloggedProcessor;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class USStructureProcessors {
    public static final CoreRegistry<StructureProcessorType<?>> PROCESSORS = CoreRegistry.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, UnvotedAndShelved.MODID);

    public static final StructureProcessorType<RandomOxidationWallProcessor> OXIDATION_WALL_PROCESSOR = register("oxidation_wall_processor", RandomOxidationWallProcessor.CODEC);
    public static final StructureProcessorType<RandomStoneReplacementProcessor> STONE_WALL_PROCESSOR = register("stone_wall_processor", RandomStoneReplacementProcessor.CODEC);
    public static final StructureProcessorType<WaterloggedProcessor> WATERLOGGED_PROCESSOR = register("waterlogged_processor", WaterloggedProcessor.CODEC);
    public static final StructureProcessorType<CloseOffFluidSourcesProcessor> CLOSE_OFF_FLUID_SOURCES_PROCESSOR = register("close_off_fluid_sources_processor", CloseOffFluidSourcesProcessor.CODEC);
    public static final StructureProcessorType<CopperPillarProcessor> COPPER_PILLAR_PROCESSOR = register("copper_pillar_processor", CopperPillarProcessor.CODEC);

    public static <P extends StructureProcessor> StructureProcessorType<P> register(String id, Codec<P> codec) {
        return PROCESSORS.register(id, () -> codec);
    }
}