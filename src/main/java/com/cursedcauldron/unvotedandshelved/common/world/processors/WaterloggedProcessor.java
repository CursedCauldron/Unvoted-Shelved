package com.cursedcauldron.unvotedandshelved.common.world.processors;

import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class WaterloggedProcessor extends StructureProcessor {
    public static final Codec<WaterloggedProcessor> CODEC = Codec.unit(WaterloggedProcessor::new);

    public WaterloggedProcessor() {}

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlaceSettings) {
        ChunkAccess chunk = levelReader.getChunk(structureBlockInfo2.pos);

        if(structureBlockInfo2.state.hasProperty(BlockStateProperties.WATERLOGGED) && !chunk.getFluidState(structureBlockInfo2.pos).isEmpty()) {
            boolean waterlog = (structureBlockInfo.state.hasProperty(BlockStateProperties.WATERLOGGED) && structureBlockInfo.state.getValue(BlockStateProperties.WATERLOGGED));

            chunk.setBlockState(structureBlockInfo2.pos, structureBlockInfo2.state.rotate(structurePlaceSettings.getRotation()).setValue(BlockStateProperties.WATERLOGGED, waterlog), false);
        }

        return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.WATERLOGGED_PROCESSOR;
    }
}
