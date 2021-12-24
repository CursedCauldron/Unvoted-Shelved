package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.GlareModel;
import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.feature.GlareGlowberriesLayer;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

//<>

@Environment(EnvType.CLIENT)
public class GlareRenderer extends MobEntityRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final Identifier NORMAL_TEXTURE = new Identifier(UnvotedAndShelved.MODID, "textures/entity/glare/glare.png");
    private static final Identifier GRUMPY_TEXTURE = new Identifier(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy.png");
    private static final Identifier LIT_TEXTURE = new Identifier(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit.png");
    private static final Identifier GRUMPY_LIT_TEXTURE = new Identifier(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit.png");



    public GlareRenderer(EntityRendererFactory.Context context) {
        super(context, new GlareModel<>(context.getPart(USEntityRenderer.GLARE)), 0.6F);
        this.addFeature(new GlareGlowberriesLayer<>(this));
    }

    @Override
    public Identifier getTexture(GlareEntity entity) {
        int d = entity.getGlowberries();
        if (d > 0) {
            return entity.isGrumpy() ? GRUMPY_LIT_TEXTURE : LIT_TEXTURE;
        } else return entity.isGrumpy() ? GRUMPY_TEXTURE : NORMAL_TEXTURE;
    }

    @Override
    protected boolean isShaking(GlareEntity entity) {
        return entity.isGrumpy();
    }
}