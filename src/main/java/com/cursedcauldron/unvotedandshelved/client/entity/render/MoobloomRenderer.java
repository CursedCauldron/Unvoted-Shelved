package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.feature.MoobloomFlowerLayer;
import com.cursedcauldron.unvotedandshelved.common.entity.MoobloomEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.data.MoobloomTypeManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class MoobloomRenderer extends MobRenderer<MoobloomEntity, CowModel<MoobloomEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_allium.png");
    private static final ResourceLocation REAL_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_real.png");

    public MoobloomRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(USEntityRenderer.MOOBLOOM)), 0.7f);
        this.addLayer(new MoobloomFlowerLayer<>(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(MoobloomEntity entity) {
        ResourceLocation texture = MoobloomTypeManager.getTexture(entity.getMoobloomType());
        if ("Get Real".equals(ChatFormatting.stripFormatting(entity.getName().getString()))) {
            return REAL_TEXTURE; // Get Real
        } else {
            //returns to allium texture if the flower index returns null
            return texture == null ? TEXTURE : texture;
        }
    }
}

