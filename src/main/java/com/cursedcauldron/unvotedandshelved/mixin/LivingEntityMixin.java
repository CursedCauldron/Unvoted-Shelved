package com.cursedcauldron.unvotedandshelved.mixin;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

// Living Entities cannot attack any Pig named Technoblade

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("RETURN"), cancellable = true)
    public void canAttack(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity.getType() == EntityType.PIG && Objects.equals(livingEntity.getName().getString(), "Technoblade")) {
            cir.setReturnValue(false);
        }
    }
}
