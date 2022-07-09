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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class WeatheringRotatedPillarBlock extends ConnectedRotatedPillarBlock implements WeatheringCopper {
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    private final WeatheringCopper.WeatherState weatherState;
    public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .put(USBlocks.COPPER_PILLAR, USBlocks.EXPOSED_COPPER_PILLAR)
            .put(USBlocks.EXPOSED_COPPER_PILLAR, USBlocks.WEATHERED_COPPER_PILLAR)
            .put(USBlocks.WEATHERED_COPPER_PILLAR, USBlocks.OXIDIZED_COPPER_PILLAR)
            .build());
    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    public WeatheringRotatedPillarBlock(WeatherState state, Properties properties) {
        super(state, properties);
        this.weatherState = state;
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTED, false));
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

    public static Optional<BlockState> getPreviousState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }
}