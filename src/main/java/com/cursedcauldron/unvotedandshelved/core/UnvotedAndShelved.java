package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.mixin.ActivityInvoker;
import com.cursedcauldron.unvotedandshelved.mixin.MemoryInvoker;
import com.google.common.reflect.Reflection;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";
    public static Activity FIND_DARKNESS = ActivityInvoker.invokeRegister("find_darkness");
    public static MemoryModuleType<Integer> SEEK_TICKS = MemoryInvoker.invokeRegister("seek_ticks", Codec.INT);
    public static MemoryModuleType<Integer> GRUMPY_TICKS = MemoryInvoker.invokeRegister("grumpy_ticks", Codec.INT);


    @Override
    public void onInitialize() {
        SoundRegistry.init();
        Reflection.initialize(
                USEntities.class
        );


        FabricDefaultAttributeRegistry.register(USEntities.GLARE, GlareEntity.createGlareAttributes());
    }
    public static Identifier ID(String path)
    {
        return new Identifier(MODID, path);
    }
}