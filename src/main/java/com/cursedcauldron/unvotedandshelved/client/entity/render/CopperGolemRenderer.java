package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.model.entity.ExampleEntityModel;
import software.bernie.example.entity.GeoExampleEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CopperGolemRenderer extends GeoEntityRenderer<CopperGolemEntity> {
    public CopperGolemRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CopperGolemModel());
    }

    @Override
    public RenderLayer getRenderType(CopperGolemEntity animatable, float partialTicks, MatrixStack stack,
                                     @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}
