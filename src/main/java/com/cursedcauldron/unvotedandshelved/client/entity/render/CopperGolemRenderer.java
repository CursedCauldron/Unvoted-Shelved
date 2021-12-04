package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

//<>

@Environment(EnvType.CLIENT)
public class CopperGolemRenderer extends MobRenderer<CopperGolemEntity, CopperGolemModel<CopperGolemEntity>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/copper_golem/copper_golem.png");

    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel<>(context.bakeLayer(CopperGolemModel.LAYER_LOCATION)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolemEntity entity) {
        return GOLEM_LOCATION;
    }

    @Override
    protected void setupRotations(CopperGolemEntity livingEntity, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(livingEntity, poseStack, f, g, h);
        if (!((double)livingEntity.animationSpeed < 0.01D)) {
            float j = livingEntity.animationPosition - livingEntity.animationSpeed * (1.0F - h) + 6.0F;
            float k = (Math.abs(j % 13.0F - 6.5F) - 3.25F) / 3.25F;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(6.5F * k));
        }
    }
}