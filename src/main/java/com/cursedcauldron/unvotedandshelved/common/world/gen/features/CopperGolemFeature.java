package com.cursedcauldron.unvotedandshelved.common.world.gen.features;

import com.cursedcauldron.unvotedandshelved.common.entity.FrozenCopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CopperGolemFeature extends Feature<NoneFeatureConfiguration> {

    public CopperGolemFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        FrozenCopperGolemEntity copperGolem = USEntities.FROZEN_COPPER_GOLEM.create(world.getLevel());
        if (copperGolem != null) {
            copperGolem.setPersistenceRequired();
            copperGolem.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            copperGolem.setYBodyRot(0);
            copperGolem.setYHeadRot(0);
            copperGolem.setXRot(0);
            copperGolem.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
            world.addFreshEntityWithPassengers(copperGolem);
        }
        return true;
    }
}
