package com.cursedcauldron.unvotedandshelved.common.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class TechnobladePigLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation textureLocation;
    private final M model;

    public TechnobladePigLayer(RenderLayerParent<T, M> renderLayerParent, M entityModel, ResourceLocation resourceLocation) {
        super(renderLayerParent);
        this.model = entityModel;
        this.textureLocation = resourceLocation;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        if (Objects.equals(entity.getName().getString(), "Technoblade")) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, f, g, h);
            this.model.setupAnim(entity, f, g, j, k, l);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(this.textureLocation));
            this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
     }
}

