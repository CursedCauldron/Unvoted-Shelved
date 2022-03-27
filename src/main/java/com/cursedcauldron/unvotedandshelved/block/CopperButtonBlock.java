package com.cursedcauldron.unvotedandshelved.block;

import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.ButtonBlock;

public class CopperButtonBlock extends ButtonBlock {

    public CopperButtonBlock(Properties properties) {
        super(false, properties);
    }

    @Override
    public int getPressDuration() {
        int duration = 0;
        if (this == USBlocks.COPPER_BUTTON.get() || this == USBlocks.WAXED_COPPER_BUTTON.get()) {
            duration = 20;
        }
        else if (this == USBlocks.EXPOSED_COPPER_BUTTON.get() || this == USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get()) {
            duration = 30;
        }
        else if (this == USBlocks.WEATHERED_COPPER_BUTTON.get() || this == USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get()) {
            duration = 40;
        }
        else if (this == USBlocks.OXIDIZED_COPPER_BUTTON.get() || this == USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()) {
            duration = 50;
        }
        return duration;
    }

    @Override
    protected SoundEvent getSound(boolean activated) {
        return USSoundEvents.COPPER_CLICK.get();
    }

}
