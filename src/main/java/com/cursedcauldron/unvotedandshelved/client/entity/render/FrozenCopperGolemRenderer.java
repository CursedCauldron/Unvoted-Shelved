package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.model.FrozenCopperGolemModel;
import com.cursedcauldron.unvotedandshelved.common.entity.FrozenCopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


@Environment(EnvType.CLIENT)
public class FrozenCopperGolemRenderer extends LivingEntityRenderer<FrozenCopperGolemEntity, FrozenCopperGolemModel<FrozenCopperGolemEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/copper_golem/oxidized_copper_golem.png");


    public FrozenCopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new FrozenCopperGolemModel<>(context.bakeLayer(USEntityRenderer.COPPER_GOLEM)), 0.5F);
    }

    @Override
    protected boolean shouldShowName(@NotNull FrozenCopperGolemEntity golem) {
        float f;
        double d = this.entityRenderDispatcher.distanceToSqr(golem);
        f = golem.isCrouching() ? 32.0f : 64.0f;
        if (d >= (double)(f * f)) {
            return false;
        }
        return golem.isCustomNameVisible();
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull FrozenCopperGolemEntity entity) {
        return TEXTURE;
    }
}
