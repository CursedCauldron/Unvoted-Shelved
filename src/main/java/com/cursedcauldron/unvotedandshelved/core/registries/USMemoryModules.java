package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.mixin.access.MemoryModuleAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Optional;

// Memory Module Registry

public class USMemoryModules {
    public static final CoreRegistry<MemoryModuleType<?>> MEMORY_MODULES = CoreRegistry.create(Registry.MEMORY_MODULE_TYPE_REGISTRY, UnvotedAndShelved.MODID);

    public static final MemoryModuleType<Integer> GRUMPY_TICKS = register("grumpy_ticks", Codec.INT);
    public static final MemoryModuleType<Integer> DARK_TICKS_REMAINING = register("darkness_ticks", Codec.INT);
    public static final MemoryModuleType<Integer> GLOWBERRIES_GIVEN = register("glowberries_given", Codec.INT);
    public static final MemoryModuleType<LivingEntity> GIVEN_GLOWBERRY = register("given_glowberry");
    public static final MemoryModuleType<BlockPos> DARK_POS = register("dark_pos");
    public static final MemoryModuleType<Integer> COPPER_BUTTON_COOLDOWN_TICKS = register("copper_button_cooldown_ticks", Codec.INT);
    public static final MemoryModuleType<Integer> COPPER_GOLEM_HEADSPIN_TICKS = register("copper_golem_headspin_ticks", Codec.INT);

    public static final MemoryModuleType<BlockPos> COPPER_BUTTON = register("copper_button");

    public static <U> MemoryModuleType<U> register(String id, Codec<U> codec) {
        return MEMORY_MODULES.register(id, MemoryModuleAccessor.createMemoryModuleType(Optional.of(codec)));
    }

    public static <U> MemoryModuleType<U> register(String id) {
        return MEMORY_MODULES.register(id, MemoryModuleAccessor.createMemoryModuleType(Optional.empty()));
    }
}