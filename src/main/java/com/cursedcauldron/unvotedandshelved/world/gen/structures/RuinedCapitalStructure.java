package com.cursedcauldron.unvotedandshelved.world.gen.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Optional;
import java.util.Random;

public class RuinedCapitalStructure extends StructureFeature<JigsawConfiguration> {

    public RuinedCapitalStructure() {
        super(JigsawConfiguration.CODEC, RuinedCapitalStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        ChunkPos chunkPos = context.chunkPos();
        int bound = 30;
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureSeed(context.seed(), chunkPos.x, chunkPos.z);
        int yLevel = -(random.nextInt(bound)) + random.nextInt(bound);
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), yLevel, chunkPos.getMinBlockZ());
        return JigsawPlacement.addPieces(context, RuinedCapitalPiece::new, blockPos, false, false);
    }

    public static class RuinedCapitalPiece extends PoolElementStructurePiece {

        public RuinedCapitalPiece(StructureManager manager, StructurePoolElement element, BlockPos pos, int groundLevelData, Rotation rotation, BoundingBox boundingBox) {
            super(manager, element, pos, groundLevelData, rotation, boundingBox);
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            if (!world.getChunk(chunkPos.x, chunkPos.z).getBlockState(pos).is(Blocks.WATER)) {
                return;
            }
            super.postProcess(world, manager, chunkGenerator, random, box, chunkPos, pos);
        }
    }

}