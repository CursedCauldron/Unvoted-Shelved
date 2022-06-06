package com.cursedcauldron.unvotedandshelved.common.blocks;


import java.util.Random;
import java.util.function.ToIntFunction;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USParticles;
import com.cursedcauldron.unvotedandshelved.core.registries.USSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.item.Items.GLASS_BOTTLE;

@SuppressWarnings("deprecation")
public class GlowberryDustBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = blockState -> blockState.getValue(LEVEL);

    public GlowberryDustBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 10).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack item = player.getItemInHand(interactionHand);
        if (!player.getAbilities().instabuild) {
            if (item.is(GLASS_BOTTLE)) {
                item.shrink(1);
                player.addItem(USBlocks.GLOWBERRY_DUST.getCloneItemStack(level, blockPos, blockState));
                level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                level.playSound(player, blockPos, USSounds.GLOWBERRY_DUST_COLLECT, SoundSource.BLOCKS, 1.0F, 1.5F);
                return InteractionResult.SUCCESS;
            }
        } else if (item.is(GLASS_BOTTLE)) {
            level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            level.playSound(player, blockPos, USSounds.GLOWBERRY_DUST_COLLECT, SoundSource.BLOCKS, 1.0F, 1.5F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }



    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionContext.isHoldingItem(GLASS_BOTTLE) ? Shapes.block() : Shapes.empty();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        int i = blockPos.getX();
        int j = blockPos.getY();
        int k = blockPos.getZ();
        double d = (double)i + random.nextDouble();
        double e = (double)j + random.nextDouble();
        double f = (double)k + random.nextDouble();
        level.addParticle(USParticles.GLOWBERRY_DUST_PARTICLES, d, e, f, 0.0, 0.0, 0.0);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int l = 0; l < 1; ++l) {
            mutableBlockPos.set(i + Mth.nextInt(random, 0, 0), j - random.nextInt(1), k + Mth.nextInt(random, 0, 0));
            BlockState blockState2 = level.getBlockState(mutableBlockPos);
            if (blockState2.isCollisionShapeFullBlock(level, mutableBlockPos)) continue;
            level.addParticle(USParticles.GLOWBERRY_DUST_PARTICLES, (double)mutableBlockPos.getX() + random.nextDouble(), (double)mutableBlockPos.getY() + random.nextDouble(), (double)mutableBlockPos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0f;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(blockState);
    }
}
