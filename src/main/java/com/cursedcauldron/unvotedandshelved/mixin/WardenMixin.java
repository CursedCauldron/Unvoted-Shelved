package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.common.entity.FrozenCopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
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

        // Wardens cannot target Pigs named Technoblade

        if (entity instanceof Pig) {
            LivingEntity livingEntity = (LivingEntity)entity;
            if (livingEntity.getType() == EntityType.PIG && Objects.equals(livingEntity.getName().getString(), "Technoblade")) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }

        // Wardens cannot target Oxidized Copper Golems

        if (entity instanceof FrozenCopperGolemEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            if (livingEntity.getType() == USEntities.FROZEN_COPPER_GOLEM) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}