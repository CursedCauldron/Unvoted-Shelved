package com.cursedcauldron.unvotedandshelved.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class CopperPipeBlock extends BaseEntityBlock implements WeatheringCopper {
    public static final BooleanProperty CORNER = BooleanProperty.create("corner");
    private final WeatherState weatherState;

    protected CopperPipeBlock(Properties properties, WeatherState weatherState) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
