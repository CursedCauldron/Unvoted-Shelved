package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.client.entity.models.ModelModifier;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PartDefinition.class)
public class PartDefinitionMixin {
    @Shadow @Final private PartPose partPose;

    @Inject(method = "bake", at = @At(value = "RETURN"), cancellable = true)
    private void US$bakeDefaultPose(int p_171584_, int p_171585_, CallbackInfoReturnable<ModelPart> cir) {
        ((ModelModifier)(Object)cir.getReturnValue()).setDefaultPose(this.partPose);
        cir.setReturnValue(cir.getReturnValue());
    }
}