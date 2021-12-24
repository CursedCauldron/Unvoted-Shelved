package com.cursedcauldron.unvotedandshelved.client.entity.render.feature;

import com.cursedcauldron.unvotedandshelved.client.entity.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GlareGlowberriesLayer<T extends GlareEntity, M extends GlareModel<T>> extends FeatureRenderer<T, M> {
    private static final RenderLayer GLARE_BERRIES = RenderLayer.getEyes(new Identifier(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_e.png"));
    private static final RenderLayer GLARE_GRUMPY_BERRIES = RenderLayer.getEyes(new Identifier(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_e.png"));

    public GlareGlowberriesLayer(FeatureRendererContext<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        RenderLayer renderType = this.renderType(entity);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            this.getContextModel().render(poseStack, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public RenderLayer renderType(GlareEntity glare){
        if (glare.getGlowberries() > 0) {
            return glare.isGrumpy() ? GLARE_GRUMPY_BERRIES : GLARE_BERRIES;
        } else return null;
    };
}