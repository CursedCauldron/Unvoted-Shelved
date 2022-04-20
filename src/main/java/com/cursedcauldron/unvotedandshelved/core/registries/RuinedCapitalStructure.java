package com.cursedcauldron.unvotedandshelved.core.registries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

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
        Random random = new Random(context.seed());
        int bound = 30;
        int yLevel = -(random.nextInt(bound)) + random.nextInt(bound);
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), yLevel, chunkPos.getMinBlockZ());
        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        context,
                        PoolElementStructurePiece::new,
                        blockPos,
                        false,
                        false
                );

        return structurePiecesGenerator;
    }

}
