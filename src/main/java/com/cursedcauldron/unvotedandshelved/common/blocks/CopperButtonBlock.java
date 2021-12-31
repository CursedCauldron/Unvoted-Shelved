package com.cursedcauldron.unvotedandshelved.common.blocks;

import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class CopperButtonBlock extends AbstractButtonBlock {

    public CopperButtonBlock(Settings settings) {
        super(false, settings);
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return SoundRegistry.COPPER_CLICK;
    }

    @Override
    protected void playClickSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, boolean powered) {
        world.playSound(powered ? player : null, pos, this.getClickSound(powered), SoundCategory.BLOCKS, 0.3F, powered ? 1.0F : 0.9F);
    }
}
