package com.cursedcauldron.unvotedandshelved.world;

import com.cursedcauldron.unvotedandshelved.init.USBiomeModifiers;
import com.cursedcauldron.unvotedandshelved.init.USEntityTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class USBiomeModifier implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (biome.is(Biomes.LUSH_CAVES) && phase == Phase.ADD) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(USEntityTypes.GLARE.get(), 10, 1, 1));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return USBiomeModifiers.US_BIOME_MODIFIER.get();
    }
}
