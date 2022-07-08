package com.cursedcauldron.unvotedandshelved.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("RETURN"), cancellable = true)
    public void canAttack(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof Pig pig && pig.getName().getString().equals("Technoblade")) {
            cir.setReturnValue(false);
        }
    }

}
