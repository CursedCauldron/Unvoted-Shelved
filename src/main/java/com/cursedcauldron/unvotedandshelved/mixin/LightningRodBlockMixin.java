package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.api.LightningRodAccess;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

// Mixin to allow for Copper Golems to be built using 1 Copper Block, 1 Carved Pumpkin/Jack-O-Lantern, and 1 Lightning Rod

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin extends Block implements LightningRodAccess, WeatheringCopper {
    @Nullable
    private BlockPattern copperGolemPattern;
    @Nullable
    private BlockPattern copperGolemDispenserPattern;
    private static final Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = (state) -> state != null && (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN));
    private static final Predicate<BlockState> IS_GOLEM_HEAD_TIP_PREDICATE = (state) -> state != null && (state == Blocks.LIGHTNING_ROD.defaultBlockState().setValue(LightningRodBlock.FACING, Direction.UP));
    private static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .put(Blocks.LIGHTNING_ROD, USBlocks.EXPOSED_LIGHTNING_ROD)
            .put(USBlocks.EXPOSED_LIGHTNING_ROD, USBlocks.WEATHERED_LIGHTNING_ROD)
            .put(USBlocks.WEATHERED_LIGHTNING_ROD, USBlocks.OXIDIZED_LIGHTNING_ROD)
            .build());
    private static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    public LightningRodBlockMixin(Properties settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, level, pos, random);
    }

    public void onPlace(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnEntity(world, pos);
        }
    }

    public void trySpawnEntity(Level world, BlockPos pos) {
        BlockPattern.BlockPatternMatch result = this.getCopperGolemPattern().find(world, pos);
        int i;
        ServerPlayer serverPlayerEntity;
        int j;
        if (result != null) {
            for (i = 0; i < this.getCopperGolemPattern().getHeight(); ++i) {
                BlockInWorld cachedBlockPosition = result.getBlock(0, i, 0);
                world.setBlock(cachedBlockPosition.getPos(), Blocks.AIR.defaultBlockState(), 2);
                world.levelEvent(2001, cachedBlockPosition.getPos(), Block.getId(cachedBlockPosition.getState()));
            }

            CopperGolemEntity e = USEntities.COPPER_GOLEM.create(world);
            BlockPos cachedBlockPosition = result.getBlock(0, 2, 0).getPos();
            assert e != null;
            e.moveTo((double) cachedBlockPosition.getX() + 0.5D, (double) cachedBlockPosition.getY() + 0.05D, (double) cachedBlockPosition.getZ() + 0.5D, 0.0F, 0.0F);
            world.addFreshEntity(e);

            for (ServerPlayer serverPlayer : world.getEntitiesOfClass(ServerPlayer.class, e.getBoundingBox().inflate(5.0D))) {
                serverPlayerEntity = serverPlayer;
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayerEntity, e);
            }

            for (j = 0; j < this.getCopperGolemPattern().getHeight(); ++j) {
                BlockInWorld position = result.getBlock(0, j, 0);
                world.blockUpdated(position.getPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getCopperGolemDispenserPattern() {
        if (this.copperGolemDispenserPattern == null) {
            this.copperGolemDispenserPattern = BlockPatternBuilder.start().aisle(" ", "^", "#").where('^', BlockInWorld.hasState(IS_GOLEM_HEAD_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.COPPER_BLOCK))).build();
        }

        return this.copperGolemDispenserPattern;
    }

    private BlockPattern getCopperGolemPattern() {
        if (this.copperGolemPattern == null) {
            this.copperGolemPattern = BlockPatternBuilder.start().aisle("^", "*", "#").where('^', BlockInWorld.hasState(IS_GOLEM_HEAD_TIP_PREDICATE)).where('*', BlockInWorld.hasState(IS_GOLEM_HEAD_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.COPPER_BLOCK))).build();
        }

        return this.copperGolemPattern;
    }

    @Override
    public boolean canDispense(LevelReader worldView, BlockPos pos) {
        return this.getCopperGolemDispenserPattern().find(worldView, pos) != null;
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }

    @Override
    public Optional<BlockState> getNext(BlockState blockState) {
        return WeatheringCopper.getNext(blockState.getBlock()).map(block -> block.withPropertiesOf(blockState));
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
    }
}
