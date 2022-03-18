package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.models.GlareModel;
import com.cursedcauldron.unvotedandshelved.client.entity.render.feature.GlareGlowberriesLayer;
import com.cursedcauldron.unvotedandshelved.entities.GlareEntity;
import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.init.USModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//<>
@OnlyIn(Dist.CLIENT)
public class GlareRenderer extends MobRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare.png");
    private static final ResourceLocation GRUMPY_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy.png");
    private static final ResourceLocation LIT_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit.png");
    private static final ResourceLocation GRUMPY_LIT_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit.png");

    public GlareRenderer(EntityRendererProvider.Context context) {
        super(context, new GlareModel<>(context.bakeLayer(USModelLayers.GLARE)), 0.6F);
        this.addLayer(new GlareGlowberriesLayer<>(this));
    }

    @Override
    protected boolean isShaking(GlareEntity entity) {
        return entity.isGrumpy();
    }

    @Override
    public ResourceLocation getTextureLocation(GlareEntity entity) {
        if (entity.getGlowberries() > 0) {
            return entity.isGrumpy() ? GRUMPY_LIT_TEXTURE : LIT_TEXTURE;
        } else {
            return entity.isGrumpy() ? GRUMPY_TEXTURE : NORMAL_TEXTURE;
        }
    }
}