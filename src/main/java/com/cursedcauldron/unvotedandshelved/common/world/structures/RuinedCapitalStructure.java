package com.cursedcauldron.unvotedandshelved.common.world.structures;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Optional;

public class RuinedCapitalStructure extends StructureFeature<StructurePoolFeatureConfig> {

    public RuinedCapitalStructure(Codec<StructurePoolFeatureConfig> configCodec) {
        super(configCodec, RuinedCapitalStructure::createPiecesGenerator, PostPlacementProcessor.EMPTY);
    }

    private static boolean isFeatureChunk(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);
        int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());
        VerticalBlockSample columnOfBlocks = context.chunkGenerator().getColumnSample(spawnXZPosition.getX(), spawnXZPosition.getZ(), context.world());
        BlockState topBlock = columnOfBlocks.getState(landHeight);
        return topBlock.getFluidState().isEmpty();
    }

    public static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> createPiecesGenerator(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {

        if (!RuinedCapitalStructure.isFeatureChunk(context)) {
            return Optional.empty();
        }

        StructurePoolFeatureConfig newConfig = new StructurePoolFeatureConfig(
                () -> context.registryManager().get(Registry.STRUCTURE_POOL_KEY)
                        //Directs to the json file
                        //TODO: Add more structures for more variation
                        .get(new Identifier(UnvotedAndShelved.MODID, "ruined_capital/ruined_capital")),
                10
        );

        StructureGeneratorFactory.Context<StructurePoolFeatureConfig> newContext = new StructureGeneratorFactory.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.world(),
                context.validBiome(),
                context.structureManager(),
                context.registryManager()
        );

        int y = 30;
        BlockPos blockpos = context.chunkPos().getCenterAtY(y);

        Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> structurePiecesGenerator =
                StructurePoolBasedGenerator.generate(
                        newContext,
                        PoolStructurePiece::new,
                        blockpos,
                        false,
                        //Keep this false. Otherwise you can't control the Y Level
                        false
                );

        return structurePiecesGenerator;
    }
}
