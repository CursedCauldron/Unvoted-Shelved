package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.GlareModel;
import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

//<>

@Environment(EnvType.CLIENT)
public class GlareRenderer extends MobRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare.png");
    private static final ResourceLocation GRUMPY_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy.png");

    public GlareRenderer(EntityRendererProvider.Context context) {
        super(context, new GlareModel<>(context.bakeLayer(USEntityRenderer.GLARE)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(GlareEntity entity) {
        return entity.isGrumpy() ? GRUMPY_TEXTURE : NORMAL_TEXTURE;
    }

    @Override
    protected boolean isShaking(GlareEntity entity) {
        return entity.isGrumpy();
    }
}