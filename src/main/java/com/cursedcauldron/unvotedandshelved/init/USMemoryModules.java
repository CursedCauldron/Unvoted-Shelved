package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USMemoryModules {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, UnvotedAndShelved.MODID);

    public static final RegistryObject<MemoryModuleType<LivingEntity>> GIVEN_GLOWBERRY = register("given_glowberry");
    public static final RegistryObject<MemoryModuleType<Integer>> GRUMPY_TICKS = register("grumpy_ticks", Codec.INT);
    public static final RegistryObject<MemoryModuleType<Integer>> DARK_TICKS_REMAINING = register("darkness_ticks", Codec.INT);
    public static final RegistryObject<MemoryModuleType<Integer>> GLOWBERRIES_GIVEN = register("glowberries_given", Codec.INT);
    public static final RegistryObject<MemoryModuleType<Integer>> COPPER_BUTTON_COOLDOWN_TICKS = register("copper_button_cooldown_ticks", Codec.INT);
    public static final RegistryObject<MemoryModuleType<BlockPos>> DARK_POS = register("dark_pos");
    public static final RegistryObject<MemoryModuleType<BlockPos>> COPPER_BUTTON_POS = register("copper_button_pos");

    public static <U> RegistryObject<MemoryModuleType<U>> register(String key, Codec<U> codec) {
        return MEMORY_MODULES.register(key, () -> new MemoryModuleType<>(Optional.of(codec)));
    }

    public static <U> RegistryObject<MemoryModuleType<U>> register(String key) {
        return MEMORY_MODULES.register(key, () -> new MemoryModuleType<>(Optional.empty()));
    }

}
