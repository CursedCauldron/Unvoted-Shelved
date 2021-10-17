package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//<>

@OnlyIn(Dist.CLIENT)
public class GlareRenderer extends MobRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare.png");
    private static final ResourceLocation GRUMPY_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy.png");

    public GlareRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new GlareModel<>(), 0.6F);
    }

    @Override
    public ResourceLocation getEntityTexture(GlareEntity entity) {
        return entity.isGrumpy() ? GRUMPY_TEXTURE : NORMAL_TEXTURE;
    }

    @Override
    protected boolean func_230495_a_(GlareEntity entity) {
        return entity.isGrumpy();
    }
}