package com.cursedcauldron.unvotedandshelved.common.blocks;

import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class WeatheringCopperButtonBlock extends CopperButtonBlock implements WeatheringCopper {
    public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .put(USBlocks.COPPER_BUTTON, USBlocks.EXPOSED_COPPER_BUTTON)
            .put(USBlocks.EXPOSED_COPPER_BUTTON, USBlocks.WEATHERED_COPPER_BUTTON)
            .put(USBlocks.WEATHERED_COPPER_BUTTON, USBlocks.OXIDIZED_COPPER_BUTTON)
            .build());
    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());
    private final WeatheringCopper.WeatherState weatherState;

    public WeatheringCopperButtonBlock(WeatheringCopper.WeatherState weatherState, Properties settings) {
        super(weatherState, settings);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));

    }

    public static Optional<BlockState> getPreviousState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state).setValue(POWERED, false));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos blockPos, RandomSource random) {
        this.onRandomTick(state, world, blockPos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState blockState) {
        return WeatheringCopper.getNext(blockState.getBlock()).map(block -> block.withPropertiesOf(blockState).setValue(POWERED, false));
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}