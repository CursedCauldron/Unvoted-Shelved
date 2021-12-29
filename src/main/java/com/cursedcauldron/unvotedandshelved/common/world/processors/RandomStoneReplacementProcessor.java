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

public class RandomStoneReplacementProcessor extends StructureProcessor {
    public static final RandomStoneReplacementProcessor INSTANCE = new RandomStoneReplacementProcessor();
    public static final Codec<RandomStoneReplacementProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        float f = random.nextFloat();
        BlockState state = structureBlockInfo2.state;
        Block randomBlock;
        float chances = 0.5F;
        if (state.isOf(Blocks.STONE_BRICKS)) {
            randomBlock = random.nextFloat() < chances ? Blocks.MOSSY_STONE_BRICKS : Blocks.MOSSY_COBBLESTONE;
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, randomBlock.getDefaultState());
        }
        else if (state.isOf(Blocks.STONE_BRICK_SLAB)) {
            randomBlock = random.nextFloat() < chances ? Blocks.MOSSY_STONE_BRICK_SLAB : Blocks.MOSSY_COBBLESTONE_SLAB;
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, randomBlock.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)));
        }
        else if (state.isOf(Blocks.STONE_BRICK_STAIRS)) {
            randomBlock = random.nextFloat() < chances ? Blocks.MOSSY_STONE_BRICK_STAIRS : Blocks.MOSSY_COBBLESTONE_STAIRS;
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, randomBlock.getDefaultState().with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE)).with(StairsBlock.HALF, state.get(StairsBlock.HALF)).with(StairsBlock.FACING, state.get(StairsBlock.FACING)));
        }
        return structureBlockInfo2;
    }

    private Structure.StructureBlockInfo setBlock(Structure.StructureBlockInfo structureBlockInfo2, float f, BlockState weatheredCopper) {
        if (f < 0.5F) {
            structureBlockInfo2 = new Structure.StructureBlockInfo(structureBlockInfo2.pos, weatheredCopper, structureBlockInfo2.nbt);
        }
        return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.STONE_WALL_PROCESSOR;
    }
}
