package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USGeoEntities;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.Material;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {
    @Nullable
    private BlockPattern copperGolemPattern;
    @Nullable
    private BlockPattern copperGolemDispenserPattern;
    private static final Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = (state) -> state != null && (state.isOf(Blocks.CARVED_PUMPKIN) || state.isOf(Blocks.JACK_O_LANTERN));
    private static final Predicate<BlockState> IS_GOLEM_HEAD_TIP_PREDICATE = (state) -> state != null && (state == Blocks.LIGHTNING_ROD.getDefaultState().with(LightningRodBlock.FACING, Direction.UP));

    @Inject(at = @At("HEAD"), method = "canDispense", cancellable = true)
    public void canDispense(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.getCopperGolemDispenserPattern().searchAround(world, pos) != null) {
            cir.setReturnValue(true);
        }
    }


    @Inject(at = @At("HEAD"), method = "trySpawnEntity")
    public void trySpawnEntity(World world, BlockPos pos, CallbackInfo ci) {
        BlockPattern.Result result = this.getCopperGolemPattern().searchAround(world, pos);
        int i;
        ServerPlayerEntity serverPlayerEntity;
        int j;
        if (result != null) {
            for (i = 0; i < this.getCopperGolemPattern().getHeight(); ++i) {
                CachedBlockPosition cachedBlockPosition = result.translate(0, i, 0);
                world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), 2);
                world.syncWorldEvent(2001, cachedBlockPosition.getBlockPos(), Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
            }

            CopperGolemEntity e = USGeoEntities.COPPER_GOLEM.create(world);
            BlockPos cachedBlockPosition = result.translate(0, 2, 0).getBlockPos();
            e.refreshPositionAndAngles((double) cachedBlockPosition.getX() + 0.5D, (double) cachedBlockPosition.getY() + 0.05D, (double) cachedBlockPosition.getZ() + 0.5D, 0.0F, 0.0F);
            world.spawnEntity(e);
            Iterator<ServerPlayerEntity> var6 = world.getNonSpectatingEntities(ServerPlayerEntity.class, e.getBoundingBox().expand(5.0D)).iterator();

            while (var6.hasNext()) {
                serverPlayerEntity = var6.next();
                Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, e);
            }

            for (j = 0; j < this.getCopperGolemPattern().getHeight(); ++j) {
                CachedBlockPosition position = result.translate(0, j, 0);
                world.updateNeighbors(position.getBlockPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getCopperGolemDispenserPattern() {
        if (this.copperGolemDispenserPattern == null) {
            this.copperGolemDispenserPattern = BlockPatternBuilder.start().aisle("^", " ", "#").where('^', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_TIP_PREDICATE)).where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.COPPER_BLOCK))).build();
        }

        return this.copperGolemDispenserPattern;
    }

    private BlockPattern getCopperGolemPattern() {
        if (this.copperGolemPattern == null) {
            this.copperGolemPattern = BlockPatternBuilder.start().aisle("^", "*", "#").where('^', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_TIP_PREDICATE)).where('*', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_PREDICATE)).where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.COPPER_BLOCK))).build();
        }

        return this.copperGolemPattern;
    }

}
