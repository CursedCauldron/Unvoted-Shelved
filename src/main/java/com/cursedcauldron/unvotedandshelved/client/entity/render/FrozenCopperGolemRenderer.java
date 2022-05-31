package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.client.entity.models.FrozenCopperGolemModel;
import com.cursedcauldron.unvotedandshelved.entities.FrozenCopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.init.USModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrozenCopperGolemRenderer extends LivingEntityRenderer<FrozenCopperGolemEntity, FrozenCopperGolemModel<FrozenCopperGolemEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/copper_golem/oxidized_copper_golem.png");

    public FrozenCopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new FrozenCopperGolemModel<>(context.bakeLayer(USModelLayers.FROZEN_COPPER_GOLEM)), 0.5F);
    }

    @Override
    protected boolean shouldShowName(FrozenCopperGolemEntity golem) {
        float f;
        double d = this.entityRenderDispatcher.distanceToSqr(golem);
        float f2 = f = golem.isCrouching() ? 32.0f : 64.0f;
        if (d >= (double)(f * f)) {
            return false;
        }
        return golem.isCustomNameVisible();
    }

    @Override
    public ResourceLocation getTextureLocation(FrozenCopperGolemEntity entity) {
        return TEXTURE;
    }
}
