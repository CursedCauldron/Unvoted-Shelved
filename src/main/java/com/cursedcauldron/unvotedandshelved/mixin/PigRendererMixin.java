package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.client.entity.render.TechnobladePigLayer;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigRenderer.class)
public class PigRendererMixin {

    // Adds a render layer to the Pig model when it is named Technoblade

    @Inject(method = "<init>", at = @At("TAIL"))
    public void PigRenderer(EntityRendererProvider.Context context, CallbackInfo ci) {
        PigRenderer $this = (PigRenderer) (Object) this;
        $this.addLayer(new TechnobladePigLayer<>($this, new PigModel<>(context.bakeLayer(ModelLayers.PIG_SADDLE))));
    }

}
