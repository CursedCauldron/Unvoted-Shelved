package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.common.entity.TechnobladePigLayer;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigRenderer.class)
public class PigRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PigRenderer(EntityRendererProvider.Context context, CallbackInfo ci) {
        PigRenderer $this = PigRenderer.class.cast(this);
        $this.addLayer(new TechnobladePigLayer($this, new PigModel(context.bakeLayer(ModelLayers.PIG_SADDLE)), new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/pig/pig_technoblade.png")));
    }
}
