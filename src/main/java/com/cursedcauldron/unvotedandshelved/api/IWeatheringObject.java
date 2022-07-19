package com.cursedcauldron.unvotedandshelved.api;

import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface IWeatheringObject extends IWaxableObject {

    Optional<BlockState> getPrevState(BlockState state);

}