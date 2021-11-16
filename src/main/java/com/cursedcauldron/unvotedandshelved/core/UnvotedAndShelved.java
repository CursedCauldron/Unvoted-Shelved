package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.mixin.ActivityInvoker;
import com.cursedcauldron.unvotedandshelved.mixin.LivingEntityMemoryInvoker;
import com.cursedcauldron.unvotedandshelved.mixin.MemoryInvoker;
import com.google.common.reflect.Reflection;
import com.mojang.serialization.Codec;
import com.sun.jna.Memory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";
    public static Activity FIND_DARKNESS = ActivityInvoker.invokeRegister("find_darkness");
    public static Activity GOTO_DARKNESS = ActivityInvoker.invokeRegister("goto_darkness");
    public static MemoryModuleType<Integer> GRUMPY_TICKS = MemoryInvoker.invokeRegister("grumpy_ticks", Codec.INT);
    public static MemoryModuleType<Integer> DARK_TICKS_REMAINING = MemoryInvoker.invokeRegister("darkness_ticks", Codec.INT);
    public static MemoryModuleType<Integer> GLOWBERRIES_GIVEN = MemoryInvoker.invokeRegister("glowberries_given", Codec.INT);
    public static MemoryModuleType<LivingEntity> GIVEN_GLOWBERRY = LivingEntityMemoryInvoker.invokeRegister("given_glowberry");
    public static MemoryModuleType<BlockPos> DARK_POS = LivingEntityMemoryInvoker.invokeRegister("dark_pos");
    public static final Item GLARE_SPAWN_EGG = new SpawnEggItem(USEntities.GLARE, 7837492, 5204011, new Item.Properties().tab(CreativeModeTab.TAB_MISC));

    @Override
    public void onInitialize() {
        SoundRegistry.init();
        Reflection.initialize(USEntities.class);

        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "glare_spawn_egg"), GLARE_SPAWN_EGG);

        FabricDefaultAttributeRegistry.register(USEntities.GLARE, GlareEntity.createGlareAttributes());
    }
    public static ResourceLocation ID(String path)
    {
        return new ResourceLocation(MODID, path);
    }
}