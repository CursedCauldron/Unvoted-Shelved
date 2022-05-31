package com.cursedcauldron.unvotedandshelved.world.processors;

import com.cursedcauldron.unvotedandshelved.init.USStructureProcessors;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class RandomStoneReplacementProcessor extends StructureProcessor {
    public static final RandomStoneReplacementProcessor INSTANCE = new RandomStoneReplacementProcessor();
    public static final Codec<RandomStoneReplacementProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        float f = random.nextFloat();
        BlockState state = structureBlockInfo2.state;
        Block randomBlock;
        float chances = 0.5F;
        if (state.is(Blocks.STONE_BRICKS)) {
            randomBlock = random.nextFloat() < chances ? Blocks.MOSSY_STONE_BRICKS : Blocks.MOSSY_COBBLESTONE;
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, randomBlock.defaultBlockState());
        }
        else if (state.is(Blocks.STONE_BRICK_SLAB)) {
            randomBlock = random.nextFloat() < chances ? Blocks.MOSSY_STONE_BRICK_SLAB : Blocks.MOSSY_COBBLESTONE_SLAB;
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, randomBlock.defaultBlockState().setValue(SlabBlock.TYPE, state.getValue(SlabBlock.TYPE)));
        }
        else if (state.is(Blocks.STONE_BRICK_STAIRS)) {
            randomBlock = random.nextFloat() < chances ? Blocks.MOSSY_STONE_BRICK_STAIRS : Blocks.MOSSY_COBBLESTONE_STAIRS;
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, randomBlock.defaultBlockState().setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE)).setValue(StairBlock.HALF, state.getValue(StairBlock.HALF)).setValue(StairBlock.FACING, state.getValue(StairBlock.FACING)));
        }
        return structureBlockInfo2;
    }

    private StructureTemplate.StructureBlockInfo setBlock(StructureTemplate.StructureBlockInfo structureBlockInfo2, float f, BlockState weatheredCopper) {
        if (f < 0.5F) {
            structureBlockInfo2 = new StructureTemplate.StructureBlockInfo(structureBlockInfo2.pos, weatheredCopper, structureBlockInfo2.nbt);
        }
        return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.STONE_WALL_PROCESSOR;
    }
}