package com.cursedcauldron.unvotedandshelved.block;

import com.cursedcauldron.unvotedandshelved.api.IWeatheringObject;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class WeatheringCopperButtonBlock extends CopperButtonBlock implements WeatheringCopper, IWeatheringObject {
    public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> {
        return ImmutableBiMap.<Block, Block>builder()
                .put(USBlocks.COPPER_BUTTON.get(), USBlocks.EXPOSED_COPPER_BUTTON.get())
                .put(USBlocks.EXPOSED_COPPER_BUTTON.get(), USBlocks.WEATHERED_COPPER_BUTTON.get())
                .put(USBlocks.WEATHERED_COPPER_BUTTON.get(), USBlocks.OXIDIZED_COPPER_BUTTON.get())
                .build();
    });
    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> {
        return NEXT_BY_BLOCK.get().inverse();
    });
    private final WeatheringCopper.WeatherState weatherState;

    public WeatheringCopperButtonBlock(WeatheringCopper.WeatherState weatherState, Properties settings) {
        super(weatherState, settings);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));

    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos blockPos, Random random) {
        this.onRandomTick(state, world, blockPos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.of(NEXT_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state).setValue(POWERED, false));
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public Optional<BlockState> getPrevState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }

}