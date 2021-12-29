package com.cursedcauldron.unvotedandshelved.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public interface LightningRodAccess {

    boolean canDispense(WorldView worldView, BlockPos pos);

}
