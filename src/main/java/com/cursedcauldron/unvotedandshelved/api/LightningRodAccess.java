package com.cursedcauldron.unvotedandshelved.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public interface LightningRodAccess {
    boolean canDispense(LevelReader worldView, BlockPos pos);
}