package com.cursedcauldron.unvotedandshelved.common.world.processors;

import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
        if(!structureBlockInfo2.state.getFluidState().isEmpty()) {
            if(levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(structureBlockInfo2.pos))) {
                return structureBlockInfo2;
            }

            ChunkAccess chunk = levelReader.getChunk(structureBlockInfo2.pos);
            int minY = chunk.getMinBuildHeight();
            int maxY = chunk.getMaxBuildHeight();
            int currentY = structureBlockInfo2.pos.getY();
            if(currentY >= minY && currentY <= maxY) {
                ((LevelAccessor) levelReader).scheduleTick(structureBlockInfo2.pos, structureBlockInfo2.state.getBlock(), 0);
            }
        }
        return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.WATERLOGGED_PROCESSOR;
    }
}
