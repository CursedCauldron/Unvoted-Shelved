package com.cursedcauldron.unvotedandshelved.common.world.processors;

import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;

import java.util.Random;

// Credits to TelepathicGrunt
public class CloseOffFluidSourcesProcessor extends StructureProcessor {
    public static final Codec<CloseOffFluidSourcesProcessor> CODEC = Codec.unit(CloseOffFluidSourcesProcessor::new);

    public CloseOffFluidSourcesProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings) {

        ChunkPos currentChunkPos = new ChunkPos(infoIn2.pos);
        if (infoIn2.state.is(Blocks.STRUCTURE_VOID) || !infoIn2.state.getFluidState().isEmpty()) {
            return infoIn2;
        }

        if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(currentChunkPos)) {
            return infoIn2;
        }

        if (!Block.isShapeFullBlock(infoIn2.state.getOcclusionShape(levelReader, infoIn2.pos)) || !infoIn2.state.getMaterial().blocksMotion()) {
            ChunkAccess currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);

            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {

                mutable.set(infoIn2.pos).move(direction);
                if
                (mutable.getY() < currentChunk.getMinBuildHeight() || mutable.getY() >= currentChunk.getMaxBuildHeight()) {
                    continue;
                }

                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunk = levelReader.getChunk(mutable);
                    currentChunkPos = new ChunkPos(mutable);
                }

                LevelHeightAccessor levelHeightAccessor = currentChunk.getHeightAccessorForGeneration();
                if (levelReader instanceof WorldGenLevel && mutable.getY() >= levelHeightAccessor.getMinBuildHeight() && mutable.getY() < levelHeightAccessor.getMaxBuildHeight()) {
                    int sectionYIndex = currentChunk.getSectionIndex(mutable.getY());
                    LevelChunkSection levelChunkSection = currentChunk.getSection(sectionYIndex);
                    if (levelChunkSection == null) continue;

                    FluidState fluidState = levelChunkSection.getFluidState(SectionPos.sectionRelative(mutable.getX()), SectionPos.sectionRelative(mutable.getY()), SectionPos.sectionRelative(mutable.getZ()));

                    if (fluidState.isSource()) {
                        Random random = new WorldgenRandom(new LegacyRandomSource(0L));
                        random.setSeed(mutable.asLong() * mutable.getY());

                        levelChunkSection.setBlockState(SectionPos.sectionRelative(mutable.getX()), SectionPos.sectionRelative(mutable.getY()), SectionPos.sectionRelative(mutable.getZ()), Blocks.STONE.defaultBlockState(), false);
                    }
                }
            }
        }

        return infoIn2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.CLOSE_OFF_FLUID_SOURCES_PROCESSOR;
    }
}
