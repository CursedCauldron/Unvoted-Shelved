package com.cursedcauldron.unvotedandshelved.common.blocks;

import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CopperButtonBlock extends ButtonBlock {
    private final WeatheringCopper.WeatherState weatherState;

    public CopperButtonBlock(WeatheringCopper.WeatherState weatherState, Properties settings) {
        super(false, settings);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected SoundEvent getSound(boolean powered) {
        return USSounds.COPPER_CLICK;
    }

    @Override
    protected void playSound(@Nullable Player player, LevelAccessor world, BlockPos pos, boolean powered) {
        world.playSound(powered ? player : null, pos, this.getSound(powered), SoundSource.BLOCKS, 0.3F, powered ? 1.0F : 0.9F);
    }


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack stack = player.getItemInHand(interactionHand);
        BlockState nextState = (weatherState == WeatheringCopper.WeatherState.UNAFFECTED) ? USBlocks.COPPER_BUTTON.defaultBlockState() : (weatherState == WeatheringCopper.WeatherState.EXPOSED) ? USBlocks.EXPOSED_COPPER_BUTTON.defaultBlockState() : (weatherState == WeatheringCopper.WeatherState.WEATHERED) ? USBlocks.WEATHERED_COPPER_BUTTON.defaultBlockState() : USBlocks.OXIDIZED_COPPER_BUTTON.defaultBlockState();

        if (stack.getItem() instanceof AxeItem) {
            if (player.isSecondaryUseActive()) {
                level.setBlockAndUpdate(blockPos, nextState.setValue(POWERED, false).setValue(FACE, blockState.getValue(FACE)).setValue(FACING, blockState.getValue(FACING)));
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void press(BlockState blockState, Level level, BlockPos blockPos) {
        level.setBlock(blockPos, blockState.setValue(POWERED, true), 3);
        this.updateNeighbours(blockState, level, blockPos);
        level.scheduleTick(blockPos, this, this.getPressDuration());
    }

    private int getPressDuration() {
        if (this.weatherState == WeatheringCopper.WeatherState.UNAFFECTED) {
            return 20;
        } else if (this.weatherState == WeatheringCopper.WeatherState.EXPOSED) {
            return 30;
        } else if (this.weatherState == WeatheringCopper.WeatherState.WEATHERED) {
            return 40;
        } else return 50;
    }

}