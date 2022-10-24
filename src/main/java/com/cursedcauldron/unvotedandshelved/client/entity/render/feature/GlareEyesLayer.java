package com.cursedcauldron.unvotedandshelved.client.entity.render.feature;

import com.cursedcauldron.unvotedandshelved.client.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * checks for the current glare state and applies the correct texture
 */
public class GlareEyesLayer<T extends GlareEntity> extends RenderLayer<T, GlareModel<T>> {
    public GlareEyesLayer(RenderLayerParent<T, GlareModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource source, int light, T entity, float angle, float distance, float tickDelta, float animationProgress, float yaw, float pitch) {
        if (!entity.isInvisible() && entity.getGlowberries() != 0) {
            VertexConsumer vertices = source.getBuffer(RenderType.entityCutout(this.getTexture(entity)));
            this.getParentModel().renderToBuffer(matrices, vertices, light, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private ResourceLocation getTexture(T entity) {
        return new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_eyes_lit" + (entity.isShiny() ? "_flowering" : "") + ".png");
    }
}