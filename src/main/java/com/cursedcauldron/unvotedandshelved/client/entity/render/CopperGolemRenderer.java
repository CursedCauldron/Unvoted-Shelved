package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.client.entity.models.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.init.USModelLayers;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CopperGolemRenderer extends MobRenderer<CopperGolemEntity, CopperGolemModel<CopperGolemEntity>> {
    private static final Map<CopperGolemEntity.Stage, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), states -> {
        for (CopperGolemEntity.Stage stage : CopperGolemEntity.Stage.BY_ID) {
            states.put(stage, new ResourceLocation(UnvotedAndShelved.MODID, String.format("textures/entity/copper_golem/%s_copper_golem.png", stage.getName())));
        }
    });

    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel<>(context.bakeLayer(USModelLayers.COPPER_GOLEM)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolemEntity entity) {
        return TEXTURES.get(entity.getStage());
    }
}
