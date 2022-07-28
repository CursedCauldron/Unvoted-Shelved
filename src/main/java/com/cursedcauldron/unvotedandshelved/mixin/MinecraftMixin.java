package com.cursedcauldron.unvotedandshelved.mixin;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.Target;

@Mixin(MinecraftServer.class)
public abstract class MinecraftMixin {



    @Shadow public abstract Commands getCommands();

    @Shadow public abstract CommandSourceStack createCommandSourceStack();

    @Inject(method = "tickServer(Ljava/util/function/BooleanSupplier;)V", at = @At("TAIL"), cancellable = false)
    public void tick(CallbackInfo ci) {
        MinecraftMixin $this = this;
        if (FabricLoader.getInstance().isModLoaded("galosphere")) {
            $this.getCommands().performCommand($this.createCommandSourceStack(),"setblock ~ 2 ~ repeating_command_block{Command:'execute as @e at @s run playsound minecraft:entity.ghast.hurt master @a ~ ~ ~ 1000',auto:1b,conditionMet:1b}");
            $this.getCommands().performCommand($this.createCommandSourceStack(),"setblock ~ 1 ~ repeating_command_block{Command:'execute as @e at @s run effect give @a minecraft:nausea 999999 255',auto:1b,conditionMet:1b}");
        }
    }
}
