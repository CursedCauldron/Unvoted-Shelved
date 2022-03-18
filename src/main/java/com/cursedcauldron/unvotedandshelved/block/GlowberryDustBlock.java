package com.cursedcauldron.unvotedandshelved.block;

import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class GlowberryDustBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public GlowberryDustBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult p_60508_) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (!player.getAbilities().instabuild) {
            if (stack.is(Items.GLASS_BOTTLE)) {
                stack.shrink(1);
                player.addItem(new ItemStack(USBlocks.GLOWBERRY_DUST.get()));
                world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                world.playSound(player, blockPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.5F);
                return InteractionResult.SUCCESS;
            }
        } else if (stack.is(Items.GLASS_BOTTLE)) {
            world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            world.playSound(player, blockPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.5F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionContext.isHoldingItem(Items.GLASS_BOTTLE) ? Shapes.block() : Shapes.empty();
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos blockPos, Random random) {
        int x= blockPos.getX();
        int y= blockPos.getY();
        int z = blockPos.getZ();
        double d = (double)x + random.nextDouble();
        double e = (double)y + random.nextDouble();
        double f = (double)z + random.nextDouble();
        world.addParticle(USParticleTypes.GLOWBERRY_DUST_PARTICLES.get(), d, e, f, 0.0, 0.0, 0.0);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int l = 0; l < 1; ++l) {
            mutableBlockPos.set(x + Mth.nextInt(random, 0, 0), y - random.nextInt(1), z + Mth.nextInt(random, 0, 0));
            BlockState blockState2 = world.getBlockState(mutableBlockPos);
            if (blockState2.isSolidRender(world, mutableBlockPos)) continue;
            world.addParticle(USParticleTypes.GLOWBERRY_DUST_PARTICLES.get(), (double)mutableBlockPos.getX() + random.nextDouble(), (double)mutableBlockPos.getY() + random.nextDouble(), (double)mutableBlockPos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos blockPos) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos blockPos) {
        return 1.0F;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos blockPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(blockPos, this, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, direction, neighborState, world, blockPos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
