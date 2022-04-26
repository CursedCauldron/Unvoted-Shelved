package com.cursedcauldron.unvotedandshelved.common.blocks;

import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USConnectedPillars;
import com.cursedcauldron.unvotedandshelved.core.registries.USTags;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

//<>

public class WeatheringRotatedPillarBlock extends RotatedPillarBlock implements WeatheringCopper {
    private final WeatheringCopper.WeatherState weatherState;

    public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> {
        return ImmutableBiMap.<Block, Block>builder()
                .put(USBlocks.COPPER_PILLAR, USBlocks.EXPOSED_COPPER_PILLAR)
                .put(USBlocks.EXPOSED_COPPER_PILLAR, USBlocks.WEATHERED_COPPER_PILLAR)
                .put(USBlocks.WEATHERED_COPPER_PILLAR, USBlocks.OXIDIZED_COPPER_PILLAR)
                .build();
    });
    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> {
        return NEXT_BY_BLOCK.get().inverse();
    });

    public WeatheringRotatedPillarBlock(WeatherState state, Properties properties) {
        super(properties);
        this.weatherState = state;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        this.onRandomTick(state, level, pos, random);
    }



    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
        if (state.getValue(AXIS) == Direction.Axis.Y) {
            if (this.weatherState == WeatherState.UNAFFECTED) {
                    BlockState belowState = level.getBlockState(pos.below());
                    if (belowState.is(USTags.EXPOSED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.EXPOSED_COPPER_PILLAR_CONNECTED_UNAFFECTED.defaultBlockState());
                    } else if (belowState.is(USTags.WEATHERED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.WEATHERED_COPPER_PILLAR_CONNECTED_UNAFFECTED.defaultBlockState());
                    } else if (belowState.is(USTags.OXIDIZED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.OXIDIZED_COPPER_PILLAR_CONNECTED_UNAFFECTED.defaultBlockState());
                    } else {
                        level.setBlockAndUpdate(pos, USBlocks.COPPER_PILLAR.defaultBlockState());
                    }
                    BlockState aboveState = level.getBlockState(pos.above());
                    if (aboveState.is(USTags.EXPOSED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.COPPER_PILLAR_CONNECTED_EXPOSED.defaultBlockState());
                    } else if (aboveState.is(USTags.WEATHERED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.COPPER_PILLAR_CONNECTED_WEATHERED.defaultBlockState());
                    } else if (aboveState.is(USTags.OXIDIZED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.COPPER_PILLAR_CONNECTED_OXIDIZED.defaultBlockState());
                    }
            } else if (this.weatherState == WeatherState.EXPOSED) {
                    BlockState belowState = level.getBlockState(pos.below());
                    if (belowState.is(USTags.COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.COPPER_PILLAR_CONNECTED_EXPOSED.defaultBlockState());
                    } else if (belowState.is(USTags.WEATHERED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.WEATHERED_COPPER_PILLAR_CONNECTED_EXPOSED.defaultBlockState());
                    } else if (belowState.is(USTags.OXIDIZED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.OXIDIZED_COPPER_PILLAR_CONNECTED_EXPOSED.defaultBlockState());
                    } else {
                        level.setBlockAndUpdate(pos, USBlocks.EXPOSED_COPPER_PILLAR.defaultBlockState());
                    }
                    BlockState aboveState = level.getBlockState(pos.above());
                    if (aboveState.is(USTags.COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.EXPOSED_COPPER_PILLAR_CONNECTED_UNAFFECTED.defaultBlockState());
                    } else if (aboveState.is(USTags.WEATHERED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.EXPOSED_COPPER_PILLAR_CONNECTED_WEATHERED.defaultBlockState());
                    } else if (aboveState.is(USTags.OXIDIZED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.EXPOSED_COPPER_PILLAR_CONNECTED_OXIDIZED.defaultBlockState());
                    }
                } else if (this.weatherState == WeatherState.WEATHERED) {
                    BlockState belowState = level.getBlockState(pos.below());
                    if (belowState.is(USTags.COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.COPPER_PILLAR_CONNECTED_WEATHERED.defaultBlockState());
                    } else if (belowState.is(USTags.EXPOSED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.EXPOSED_COPPER_PILLAR_CONNECTED_WEATHERED.defaultBlockState());
                    } else if (belowState.is(USTags.OXIDIZED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.OXIDIZED_COPPER_PILLAR_CONNECTED_WEATHERED.defaultBlockState());
                    } else {
                        level.setBlockAndUpdate(pos, USBlocks.WEATHERED_COPPER_PILLAR.defaultBlockState());
                    }
                    BlockState aboveState = level.getBlockState(pos.above());
                    if (aboveState.is(USTags.EXPOSED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.WEATHERED_COPPER_PILLAR_CONNECTED_EXPOSED.defaultBlockState());
                    } else if (aboveState.is(USTags.COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.WEATHERED_COPPER_PILLAR_CONNECTED_UNAFFECTED.defaultBlockState());
                    } else if (aboveState.is(USTags.OXIDIZED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.WEATHERED_COPPER_PILLAR_CONNECTED_OXIDIZED.defaultBlockState());
                    }
                } else if (this.weatherState == WeatherState.OXIDIZED) {
                    BlockState belowState = level.getBlockState(pos.below());
                    if (belowState.is(USTags.COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.COPPER_PILLAR_CONNECTED_OXIDIZED.defaultBlockState());
                    } else if (belowState.is(USTags.EXPOSED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.EXPOSED_COPPER_PILLAR_CONNECTED_OXIDIZED.defaultBlockState());
                    } else if (belowState.is(USTags.WEATHERED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos, USConnectedPillars.WEATHERED_COPPER_PILLAR_CONNECTED_OXIDIZED.defaultBlockState());
                    } else {
                        level.setBlockAndUpdate(pos, USBlocks.OXIDIZED_COPPER_PILLAR.defaultBlockState());
                    }
                    BlockState aboveState = level.getBlockState(pos.above());
                    if (aboveState.is(USTags.EXPOSED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.OXIDIZED_COPPER_PILLAR_CONNECTED_EXPOSED.defaultBlockState());
                    } else if (aboveState.is(USTags.COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.OXIDIZED_COPPER_PILLAR_CONNECTED_UNAFFECTED.defaultBlockState());
                    } else if (aboveState.is(USTags.WEATHERED_COPPER_PILLARS)) {
                        level.setBlockAndUpdate(pos.above(), USConnectedPillars.OXIDIZED_COPPER_PILLAR_CONNECTED_WEATHERED.defaultBlockState());
                    }
                }
            }
        super.neighborChanged(state, level, pos, block, blockPos2, bl);
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }

    public static Optional<BlockState> getPreviousState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}