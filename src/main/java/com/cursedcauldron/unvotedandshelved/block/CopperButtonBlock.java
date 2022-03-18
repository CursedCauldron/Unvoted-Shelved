package com.cursedcauldron.unvotedandshelved.block;

import com.cursedcauldron.unvotedandshelved.init.USSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.ButtonBlock;

public class CopperButtonBlock extends ButtonBlock {

    public CopperButtonBlock(Properties properties) {
        super(false, properties);
    }

    @Override
    protected SoundEvent getSound(boolean activated) {
        return USSoundEvents.COPPER_CLICK.get();
    }

}
