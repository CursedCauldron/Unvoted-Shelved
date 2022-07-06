package com.cursedcauldron.unvotedandshelved.api;

import com.google.common.collect.BiMap;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IWaxableObject {

    Supplier<BiMap<Block, Block>> getWaxables();

}
