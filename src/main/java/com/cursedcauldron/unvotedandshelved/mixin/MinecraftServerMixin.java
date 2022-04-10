package com.cursedcauldron.unvotedandshelved.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyConstant(method = "prepareLevels", constant = @Constant(intValue = 11, ordinal = 0))
    private static int fastSpawn(int constant) {
        return 0;
    }

    @ModifyConstant(method = "prepareLevels", constant = @Constant(intValue = 441, ordinal = 0))
    private static int fastSpawn2(int constant) {
        return 0;
    }
}