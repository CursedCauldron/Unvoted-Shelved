package com.cursedcauldron.unvotedandshelved.common.blocks;


import java.util.Random;
import java.util.function.ToIntFunction;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.GLOWBERRY_DUST;
import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.GLOWBERRY_DUST_PARTICLES;
import static net.minecraft.item.Items.GLASS_BOTTLE;

@SuppressWarnings("deprecation")
public class GlowberryDustBlock extends Block
        implements Waterloggable {
    public static final IntProperty LEVEL = Properties.LEVEL_15;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = blockState -> blockState.get(LEVEL);

    public GlowberryDustBlock(AbstractBlock.Settings properties) {
        super(properties);
        this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL, 10).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, WATERLOGGED);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World level, BlockPos blockPos, PlayerEntity player, Hand interactionHand, BlockHitResult blockHitResult) {
        ItemStack item = player.getStackInHand(interactionHand);
        if (!player.getAbilities().creativeMode) {
            if (item.isOf(GLASS_BOTTLE)) {
                item.decrement(1);
                player.giveItemStack(GLOWBERRY_DUST.getPickStack(level, blockPos, blockState));
                level.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                level.playSound(player, blockPos, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.5F);
                return ActionResult.SUCCESS;
            }
        } else if (item.isOf(GLASS_BOTTLE)) {
            level.setBlockState(blockPos, Blocks.AIR.getDefaultState());
            level.playSound(player, blockPos, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.5F);
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }



    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        return collisionContext.isHolding(GLASS_BOTTLE) ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World level, BlockPos blockPos, Random random) {
        int i = blockPos.getX();
        int j = blockPos.getY();
        int k = blockPos.getZ();
        double d = (double)i + random.nextDouble();
        double e = (double)j + random.nextDouble();
        double f = (double)k + random.nextDouble();
        level.addParticle(GLOWBERRY_DUST_PARTICLES, d, e, f, 0.0, 0.0, 0.0);
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        for (int l = 0; l < 1; ++l) {
            mutableBlockPos.set(i + MathHelper.nextInt(random, 0, 0), j - random.nextInt(1), k + MathHelper.nextInt(random, 0, 0));
            BlockState blockState2 = level.getBlockState(mutableBlockPos);
            if (blockState2.isFullCube(level, mutableBlockPos)) continue;
            level.addParticle(GLOWBERRY_DUST_PARTICLES, (double)mutableBlockPos.getX() + random.nextDouble(), (double)mutableBlockPos.getY() + random.nextDouble(), (double)mutableBlockPos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isTranslucent(BlockState blockState, BlockView blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState blockState, BlockView blockGetter, BlockPos blockPos) {
        return 1.0f;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState blockState2, WorldAccess levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.get(WATERLOGGED)) {
            levelAccessor.createAndScheduleFluidTick(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(levelAccessor));
        }
        return super.getStateForNeighborUpdate(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(blockState);
    }
}
