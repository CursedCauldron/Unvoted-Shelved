package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.api.LightningRodAccess;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Iterator;
import java.util.function.Predicate;
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

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin extends Block implements LightningRodAccess {
    @Nullable
    private BlockPattern copperGolemPattern;
    @Nullable
    private BlockPattern copperGolemDispenserPattern;
    private static final Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = (state) -> state != null && (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN));
    private static final Predicate<BlockState> IS_GOLEM_HEAD_TIP_PREDICATE = (state) -> state != null && (state == Blocks.LIGHTNING_ROD.defaultBlockState().setValue(LightningRodBlock.FACING, Direction.UP));

    public LightningRodBlockMixin(Properties settings) {
        super(settings);
    }

    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
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
            e.moveTo((double) cachedBlockPosition.getX() + 0.5D, (double) cachedBlockPosition.getY() + 0.05D, (double) cachedBlockPosition.getZ() + 0.5D, 0.0F, 0.0F);
            world.addFreshEntity(e);
            Iterator<ServerPlayer> var6 = world.getEntitiesOfClass(ServerPlayer.class, e.getBoundingBox().inflate(5.0D)).iterator();

            while (var6.hasNext()) {
                serverPlayerEntity = var6.next();
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
}
