package com.cursedcauldron.unvotedandshelved.common.blocks;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class CopperButtonBlock extends AbstractButtonBlock {

    public CopperButtonBlock(Settings settings) {
        super(false, settings);
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return powered ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    }
}
