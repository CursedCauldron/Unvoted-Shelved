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
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * checks for the current amount of glowberries fed to the glare to apply a texture
 */
@Environment(EnvType.CLIENT)
public class GlareGlowberriesLayer<T extends GlareEntity, M extends GlareModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation[] STATES = new ResourceLocation[] {
            new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit.png"),
            new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit1.png"),
            new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit2.png"),
            new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit3.png"),
            new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit4.png")
    };

    public GlareGlowberriesLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource source, int light, T entity, float angle, float distance, float tickDelta, float animationProgress, float yaw, float pitch) {
        if (!entity.isInvisible() && entity.getGlowberries() != 0 && entity.getGlowberries() <= 5) {
            ResourceLocation location = STATES[entity.getGlowberries() - 1];
            VertexConsumer vertices = source.getBuffer(RenderType.entityTranslucentEmissive(location));
            this.getParentModel().renderToBuffer(matrices, vertices, light, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}