package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.feature.MoobloomFlowerLayer;
import com.cursedcauldron.unvotedandshelved.common.entity.Moobloom;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class MoobloomRenderer extends MobRenderer<Moobloom, CowModel<Moobloom>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_dandelion.png");

    public MoobloomRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(USEntityRenderer.MOOBLOOM)), 0.7f);
        this.addLayer(new MoobloomFlowerLayer<>(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(Moobloom entity) {
        return TEXTURE;
    }
}

