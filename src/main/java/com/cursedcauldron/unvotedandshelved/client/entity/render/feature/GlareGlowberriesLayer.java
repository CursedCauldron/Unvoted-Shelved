package com.cursedcauldron.unvotedandshelved.client.entity.render.feature;

import com.cursedcauldron.unvotedandshelved.client.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class GlareGlowberriesLayer<T extends GlareEntity, M extends GlareModel<T>> extends RenderLayer<T, M> {
    private static final RenderType GLARE_BERRIES = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_e.png"));
    private static final RenderType GLARE_BERRIES_1 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_1_e.png"));
    private static final RenderType GLARE_BERRIES_2 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_2_e.png"));
    private static final RenderType GLARE_BERRIES_3 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_3_e.png"));
    private static final RenderType GLARE_BERRIES_4 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_4_e.png"));
    private static final RenderType GLARE_GRUMPY_BERRIES = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_e.png"));
    private static final RenderType GLARE_GRUMPY_BERRIES_1 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_1_e.png"));
    private static final RenderType GLARE_GRUMPY_BERRIES_2 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_2_e.png"));
    private static final RenderType GLARE_GRUMPY_BERRIES_3 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_3_e.png"));
    private static final RenderType GLARE_GRUMPY_BERRIES_4 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_4_e.png"));

    private static final RenderType FLOWERING_GLARE_BERRIES = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_e.png"));
    private static final RenderType FLOWERING_GLARE_BERRIES_1 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_1_e.png"));
    private static final RenderType FLOWERING_GLARE_BERRIES_2 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_2_e.png"));
    private static final RenderType FLOWERING_GLARE_BERRIES_3 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_3_e.png"));
    private static final RenderType FLOWERING_GLARE_BERRIES_4 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_4_e.png"));
    private static final RenderType FLOWERING_GLARE_GRUMPY_BERRIES = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_e.png"));
    private static final RenderType FLOWERING_GLARE_GRUMPY_BERRIES_1 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_1_e.png"));
    private static final RenderType FLOWERING_GLARE_GRUMPY_BERRIES_2 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_2_e.png"));
    private static final RenderType FLOWERING_GLARE_GRUMPY_BERRIES_3 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_3_e.png"));
    private static final RenderType FLOWERING_GLARE_GRUMPY_BERRIES_4 = RenderType.eyes(new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_4_e.png"));

    public GlareGlowberriesLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float f, float g, float h, float j, float k, float l) {
        RenderType renderType = this.renderType(entity);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public RenderType renderType(GlareEntity entity){
        int d = entity.getGlowberries();
        if (d == 1) {
            if (entity.isShiny()) {
                return entity.isGrumpy() ? FLOWERING_GLARE_GRUMPY_BERRIES : FLOWERING_GLARE_BERRIES;
            } else return entity.isGrumpy() ? GLARE_GRUMPY_BERRIES : GLARE_BERRIES;
        } else if (d == 2) {
            if (entity.isShiny()) {
                return entity.isGrumpy() ? FLOWERING_GLARE_GRUMPY_BERRIES_1 : FLOWERING_GLARE_BERRIES_1;
            } else return entity.isGrumpy() ? GLARE_GRUMPY_BERRIES_1 : GLARE_BERRIES_1;
        } else if (d == 3) {
            if (entity.isShiny()) {
                return entity.isGrumpy() ? FLOWERING_GLARE_GRUMPY_BERRIES_2 : FLOWERING_GLARE_BERRIES_2;
            } else return entity.isGrumpy() ? GLARE_GRUMPY_BERRIES_2 : GLARE_BERRIES_2;
        } else if (d == 4) {
            if (entity.isShiny()) {
                return entity.isGrumpy() ? FLOWERING_GLARE_GRUMPY_BERRIES_3 : FLOWERING_GLARE_BERRIES_3;
            } else return entity.isGrumpy() ? GLARE_GRUMPY_BERRIES_3 : GLARE_BERRIES_3;
        } else if (d == 5) {
            if (entity.isShiny()) {
                return entity.isGrumpy() ? FLOWERING_GLARE_GRUMPY_BERRIES_4 : FLOWERING_GLARE_BERRIES_4;
            } else return entity.isGrumpy() ? GLARE_GRUMPY_BERRIES_4 : GLARE_BERRIES_4;
        } else return null;
    }
}