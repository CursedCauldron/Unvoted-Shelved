package com.cursedcauldron.unvotedandshelved.common.world.gen.structures;

import com.cursedcauldron.unvotedandshelved.config.FeatureScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;

public class RuinedCapitalStructure extends StructureFeature<JigsawConfiguration> {

    public RuinedCapitalStructure() {
        super(JigsawConfiguration.CODEC, RuinedCapitalStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (FabricLoader.getInstance().isModLoaded("modmenu"))  {
            ChunkPos chunkPos = context.chunkPos();
            WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
            random.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
            int bound = 30;
            int yLevel = FeatureScreen.RUINED_CAPITALS.getValue() ? -(random.nextInt(bound)) + random.nextInt(bound) : -9999;
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
        } else {
            ChunkPos chunkPos = context.chunkPos();
            WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
            random.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
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
}
