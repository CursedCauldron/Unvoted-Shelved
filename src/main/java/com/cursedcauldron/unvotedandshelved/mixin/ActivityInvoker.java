package com.cursedcauldron.unvotedandshelved.mixin;

import net.minecraft.entity.ai.brain.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Activity.class)
public interface ActivityInvoker {
    @Invoker("register")
    public static Activity invokeRegister(String id) {
        throw new AssertionError();
    }
}
