package com.cursedcauldron.unvotedandshelved.world.processors;

import com.cursedcauldron.unvotedandshelved.block.WeatheringRotatedPillarBlock;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USStructureProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class RandomOxidationWallProcessor extends StructureProcessor {
    public static final RandomOxidationWallProcessor INSTANCE = new RandomOxidationWallProcessor();
    public static final Codec<RandomOxidationWallProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings data) {
        RandomSource random = data.getRandom(structureBlockInfo2.pos);
        float f = random.nextFloat();
        BlockState state = structureBlockInfo2.state;
        if (state.is(Blocks.OXIDIZED_CUT_COPPER)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_CUT_COPPER.defaultBlockState());
        }
        else if (state.is(Blocks.OXIDIZED_COPPER)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_COPPER.defaultBlockState());
        }
        else if (state.is(Blocks.OXIDIZED_CUT_COPPER_STAIRS)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_CUT_COPPER_STAIRS.defaultBlockState().setValue(StairBlock.HALF, state.getValue(StairBlock.HALF)).setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE)).setValue(StairBlock.FACING, state.getValue(StairBlock.FACING)));
        }
        else if (state.is(Blocks.OXIDIZED_CUT_COPPER_SLAB)) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, Blocks.WEATHERED_CUT_COPPER_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, state.getValue(SlabBlock.TYPE)));
        }
        else if (state.is(USBlocks.OXIDIZED_COPPER_PILLAR.get())) {
            structureBlockInfo2 = this.setBlock(structureBlockInfo2, f, USBlocks.WEATHERED_COPPER_PILLAR.get().defaultBlockState().setValue(WeatheringRotatedPillarBlock.AXIS, state.getValue(WeatheringRotatedPillarBlock.AXIS)).setValue(WeatheringRotatedPillarBlock.CONNECTED, state.getValue(WeatheringRotatedPillarBlock.CONNECTED)));
        }
        return structureBlockInfo2;
    }

    private StructureTemplate.StructureBlockInfo setBlock(StructureTemplate.StructureBlockInfo structureBlockInfo2, float f, BlockState block) {
        if (f < 0.5F) {
            structureBlockInfo2 = new StructureTemplate.StructureBlockInfo(structureBlockInfo2.pos, block, structureBlockInfo2.nbt);
        }
        return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.OXIDATION_WALL_PROCESSOR;
    }
}