package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.client.entity.render.GlareRenderer;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

//<>

@Environment(EnvType.CLIENT)
public class USEntityRenderer {
    public static final EntityModelLayer GLARE = new EntityModelLayer(new Identifier(UnvotedAndShelved.MODID, "glare"), "main");

    public static void registerRenderers() {
        EntityRendererRegistry.register(USEntities.GLARE, GlareRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(GLARE, GlareModel::getTexturedModelData);
    }
}