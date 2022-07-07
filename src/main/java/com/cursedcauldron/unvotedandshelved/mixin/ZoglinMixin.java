package com.cursedcauldron.unvotedandshelved.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Zoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Zoglin.class)
public class ZoglinMixin {
    @Inject(method = "isTargetable(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("RETURN"), cancellable = true)
    private void isTargetable(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        EntityType<?> entityType = livingEntity.getType();
        if (entityType == EntityType.PIG && Objects.equals(livingEntity.getName().getString(), "Technoblade")) {
            cir.setReturnValue(false);
        }
    }
}
