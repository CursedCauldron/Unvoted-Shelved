package com.cursedcauldron.unvotedandshelved.mixin;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MemoryModuleType.class)
public interface LivingEntityMemoryInvoker {
    @Invoker("register")
    static <U> MemoryModuleType<U> invokeRegister(String string) {
        throw new AssertionError();
    }
}
