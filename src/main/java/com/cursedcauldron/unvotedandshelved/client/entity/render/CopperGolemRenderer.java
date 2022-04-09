package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;


@Environment(EnvType.CLIENT)
public class CopperGolemRenderer extends MobRenderer<CopperGolemEntity, CopperGolemModel<CopperGolemEntity>> {
    private static final Map<CopperGolemEntity.Stage, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), states -> {
        for (CopperGolemEntity.Stage stage : CopperGolemEntity.Stage.BY_ID) {
            states.put(stage, new ResourceLocation(UnvotedAndShelved.MODID, String.format("textures/entity/copper_golem/%s_copper_golem.png", stage.getName())));
        }
    });

    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel<>(context.bakeLayer(USEntityRenderer.COPPER_GOLEM)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolemEntity entity) {
        return TEXTURES.get(entity.getStage());
    }
}
