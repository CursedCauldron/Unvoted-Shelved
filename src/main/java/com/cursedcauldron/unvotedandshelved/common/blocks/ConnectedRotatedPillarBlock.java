package com.cursedcauldron.unvotedandshelved.common.blocks;

import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USTags;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

//<>

public class ConnectedRotatedPillarBlock extends RotatedPillarBlock implements WeatheringCopper {
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    private final WeatherState weatherState;

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


    public ConnectedRotatedPillarBlock(WeatherState state, Properties properties) {
        super(properties);
        this.weatherState = state;
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTED, false));
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
       BlockPos abovePos  = (state.getValue(AXIS) == Direction.Axis.Y) ? pos.above() : (state.getValue(AXIS) == Direction.Axis.X) ? pos.east() : pos.north();
       BlockState aboveState = level.getBlockState(abovePos);

       BlockPos belowPos  = (state.getValue(AXIS) == Direction.Axis.Y) ? pos.below() : (state.getValue(AXIS) == Direction.Axis.X) ? pos.west() : pos.south();
       BlockState belowState = level.getBlockState(belowPos);

       if (aboveState.is(USTags.COPPER_PILLARS) && aboveState.getValue(AXIS) == state.getValue(AXIS)) {
               level.setBlock(pos, state.setValue(CONNECTED, true), 3);
           } else {
               level.setBlock(pos, state.setValue(CONNECTED, false), 3);
           }
           if (belowState.is(USTags.COPPER_PILLARS) && belowState.getValue(AXIS) == state.getValue(AXIS)) {
               level.setBlock(belowPos, belowState.setValue(CONNECTED, true), 3);
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }
}