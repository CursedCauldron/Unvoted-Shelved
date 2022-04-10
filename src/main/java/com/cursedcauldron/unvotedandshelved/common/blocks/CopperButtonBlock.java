package com.cursedcauldron.unvotedandshelved.common.blocks;

import com.cursedcauldron.unvotedandshelved.core.registries.USSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ButtonBlock;
import org.jetbrains.annotations.Nullable;

public class CopperButtonBlock extends ButtonBlock {

    public CopperButtonBlock(Properties settings) {
        super(false, settings);
    }

    @Override
    protected SoundEvent getSound(boolean powered) {
        return USSounds.COPPER_CLICK;
    }

    @Override
    protected void playSound(@Nullable Player player, LevelAccessor world, BlockPos pos, boolean powered) {
        world.playSound(powered ? player : null, pos, this.getSound(powered), SoundSource.BLOCKS, 0.3F, powered ? 1.0F : 0.9F);
    }
}
