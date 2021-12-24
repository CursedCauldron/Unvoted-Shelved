package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlock;
import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlockItem;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.core.registries.USGeoEntities;
import com.cursedcauldron.unvotedandshelved.mixin.ActivityInvoker;
import com.cursedcauldron.unvotedandshelved.mixin.LivingEntityMemoryInvoker;
import com.cursedcauldron.unvotedandshelved.mixin.MemoryInvoker;
import com.google.common.reflect.Reflection;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import software.bernie.example.EntityUtils;
import software.bernie.geckolib3.GeckoLib;


import static com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry.GLOWBERRY_DUST_STEP;
import static com.cursedcauldron.unvotedandshelved.core.registries.USEntities.GLARE;
import static net.minecraft.world.biome.BiomeKeys.LUSH_CAVES;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";
    public static Activity GOTO_DARKNESS = ActivityInvoker.invokeRegister("goto_darkness");
    public static MemoryModuleType<Integer> GRUMPY_TICKS = MemoryInvoker.invokeRegister("grumpy_ticks", Codec.INT);
    public static MemoryModuleType<Integer> DARK_TICKS_REMAINING = MemoryInvoker.invokeRegister("darkness_ticks", Codec.INT);
    public static MemoryModuleType<Integer> GLOWBERRIES_GIVEN = MemoryInvoker.invokeRegister("glowberries_given", Codec.INT);
    public static MemoryModuleType<LivingEntity> GIVEN_GLOWBERRY = LivingEntityMemoryInvoker.invokeRegister("given_glowberry");
    public static MemoryModuleType<BlockPos> DARK_POS = LivingEntityMemoryInvoker.invokeRegister("dark_pos");
    public static final Item GLARE_SPAWN_EGG = new SpawnEggItem(GLARE, 7837492, 5204011, new Item.Settings().group(ItemGroup.MISC));
    public static final BlockSoundGroup GLOW = new BlockSoundGroup(1.0f, 2.0f, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, GLOWBERRY_DUST_STEP, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE , SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE);
    public static GlowberryDustBlock GLOWBERRY_DUST = new GlowberryDustBlock(AbstractBlock.Settings.of(Material.AIR).strength(-1.0f, 3600000.8f).dropsNothing().sounds(GLOW).luminance(GlowberryDustBlock.LIGHT_EMISSION));
    public static final DefaultParticleType GLOWBERRY_DUST_PARTICLES = register("glowberry_dust", false);


    public static DefaultParticleType register(String key, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier(MODID, key), new AbstractSimpleParticleType(alwaysShow));
    }



    @Override
    public void onInitialize() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(LUSH_CAVES), SpawnGroup.UNDERGROUND_WATER_CREATURE, GLARE, 10, 1, 1);
        SoundRegistry.init();
        GeckoLib.initialize();
        Reflection.initialize(USEntities.class);
        Registry.register(Registry.ITEM, new Identifier(MODID, "glare_spawn_egg"), GLARE_SPAWN_EGG);
        Registry.register(Registry.ITEM, new Identifier(MODID, "glowberry_dust_bottle"), new GlowberryDustBlockItem(GLOWBERRY_DUST, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        GLOWBERRY_DUST = Registry.register(Registry.BLOCK, new Identifier(MODID, "glowberry_dust"), GLOWBERRY_DUST);
        FabricDefaultAttributeRegistry.register(GLARE, GlareEntity.createGlareAttributes());
        new USGeoEntities();
        FabricDefaultAttributeRegistry.register(USGeoEntities.COPPER_GOLEM,
                EntityUtils.createGenericEntityAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.7F));
    }
    public static Identifier ID(String path)
    {
        return new Identifier(MODID, path);
    }
}