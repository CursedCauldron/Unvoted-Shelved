package com.cursedcauldron.unvotedandshelved.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Warden.class)
public class WardenMixin {

    @Inject(method = "canTargetEntity(Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
    public void canTargetEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Pig pig && Objects.equals(pig.getName().getString(), "Technoblade")) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
