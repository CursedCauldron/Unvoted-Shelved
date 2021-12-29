package com.cursedcauldron.unvotedandshelved.common.world.processors;

import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class RandomOxidationWallProcessor extends StructureProcessor {
    public static final RandomOxidationWallProcessor INSTANCE = new RandomOxidationWallProcessor();
    public static final Codec<RandomOxidationWallProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        float f = random.nextFloat();
        BlockState state = structureBlockInfo2.state;
        if (state.isOf(Blocks.OXIDIZED_CUT_COPPER)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_CUT_COPPER.getDefaultState());
        }
        else if (state.isOf(Blocks.OXIDIZED_COPPER)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_COPPER.getDefaultState());
        }
        else if (state.isOf(Blocks.OXIDIZED_CUT_COPPER_STAIRS)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_CUT_COPPER_STAIRS.getDefaultState().with(StairsBlock.HALF, state.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE)).with(StairsBlock.FACING, state.get(StairsBlock.FACING)));
        }
        else if (state.isOf(Blocks.OXIDIZED_CUT_COPPER_SLAB)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_CUT_COPPER_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)));
        }
        return structureBlockInfo2;
    }

    private Structure.StructureBlockInfo setBlock(Structure.StructureBlockInfo structureBlockInfo2, float f, BlockState block) {
        if (f < 0.5F) {
            structureBlockInfo2 = new Structure.StructureBlockInfo(structureBlockInfo2.pos, block, structureBlockInfo2.nbt);
        }
        return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.OXIDATION_WALL_PROCESSOR;
    }
}
