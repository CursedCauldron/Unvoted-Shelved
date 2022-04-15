package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.model.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.entity.model.FrozenCopperGolemModel;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.FrozenCopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;


@Environment(EnvType.CLIENT)
public class FrozenCopperGolemRenderer extends LivingEntityRenderer<FrozenCopperGolemEntity, FrozenCopperGolemModel<FrozenCopperGolemEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/copper_golem/oxidized_copper_golem.png");


    public FrozenCopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new FrozenCopperGolemModel<>(context.bakeLayer(USEntityRenderer.COPPER_GOLEM)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(FrozenCopperGolemEntity entity) {
        return TEXTURE;
    }
}
