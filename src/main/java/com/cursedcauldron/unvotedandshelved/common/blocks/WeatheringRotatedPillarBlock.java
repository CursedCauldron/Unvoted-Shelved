package com.cursedcauldron.unvotedandshelved.common.blocks;

import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
    private WeatheringCopper.WeatherState connectionState;

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

    public WeatheringRotatedPillarBlock(WeatherState state, WeatherState connectionState, Properties properties) {
        super(properties);
        this.weatherState = state;
        this.connectionState = connectionState;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        this.onRandomTick(state, level, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (this.weatherState == WeatherState.UNAFFECTED) {
            if (state.getValue(AXIS) == Direction.Axis.Y) {
                BlockState belowState = level.getBlockState(pos.below());
                if (belowState.is(USBlocks.EXPOSED_COPPER_PILLAR)) {
                    level.setBlockAndUpdate(pos, USBlocks.COPPER_PILLAR_CONNECTED_EXPOSED.defaultBlockState());
                } else if (belowState.is(USBlocks.WEATHERED_COPPER_PILLAR)) {
                    level.setBlockAndUpdate(pos, USBlocks.COPPER_PILLAR_CONNECTED_WEATHERED.defaultBlockState());
                } else if (belowState.is(USBlocks.OXIDIZED_COPPER_PILLAR)) {
                    level.setBlockAndUpdate(pos, USBlocks.COPPER_PILLAR_CONNECTED_OXIDIZED.defaultBlockState());
                }
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
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