package com.cursedcauldron.unvotedandshelved.block;

import com.cursedcauldron.unvotedandshelved.api.IWeatheringObject;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class WeatheringLightningRodBlock extends LightningRodBlock implements WeatheringCopper, IWeatheringObject {
    private final WeatheringCopper.WeatherState weatherState;
    public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .put(Blocks.LIGHTNING_ROD, USBlocks.EXPOSED_LIGHTNING_ROD.get())
            .put(USBlocks.EXPOSED_LIGHTNING_ROD.get(), USBlocks.WEATHERED_LIGHTNING_ROD.get())
            .put(USBlocks.WEATHERED_LIGHTNING_ROD.get(), USBlocks.OXIDIZED_LIGHTNING_ROD.get())
            .build());
    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());
    public static final Supplier<BiMap<Block, Block>> WAXABLES = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder().put(Blocks.LIGHTNING_ROD, USBlocks.WAXED_LIGHTNING_ROD.get()).put(USBlocks.EXPOSED_LIGHTNING_ROD.get(), USBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get()).put(USBlocks.WEATHERED_LIGHTNING_ROD.get(), USBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get()).put(USBlocks.OXIDIZED_LIGHTNING_ROD.get(), USBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get()).build());

    public WeatheringLightningRodBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public Optional<BlockState> getPrevState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }

    @Override
    public Supplier<BiMap<Block, Block>> getWaxables() {
        return WAXABLES;
    }
}