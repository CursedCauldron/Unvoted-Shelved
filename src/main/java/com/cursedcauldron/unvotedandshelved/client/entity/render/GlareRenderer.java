package com.cursedcauldron.unvotedandshelved.client.entity.render;

import com.cursedcauldron.unvotedandshelved.client.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.feature.GlareGlowberriesLayer;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class GlareRenderer extends MobRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare.png");
    private static final ResourceLocation GRUMPY_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy.png");
    private static final ResourceLocation LIT_TEXTURE_1 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit.png");
    private static final ResourceLocation LIT_TEXTURE_2 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_1.png");
    private static final ResourceLocation LIT_TEXTURE_3 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_2.png");
    private static final ResourceLocation LIT_TEXTURE_4 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_3.png");
    private static final ResourceLocation LIT_TEXTURE_5 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_lit_4.png");

    private static final ResourceLocation GRUMPY_LIT_TEXTURE_1 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit.png");
    private static final ResourceLocation GRUMPY_LIT_TEXTURE_2 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_1.png");
    private static final ResourceLocation GRUMPY_LIT_TEXTURE_3 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_2.png");
    private static final ResourceLocation GRUMPY_LIT_TEXTURE_4 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_3.png");
    private static final ResourceLocation GRUMPY_LIT_TEXTURE_5 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/glare_grumpy_lit_4.png");

    private static final ResourceLocation FLOWERING_NORMAL_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare.png");
    private static final ResourceLocation FLOWERING_GRUMPY_TEXTURE = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy.png");
    private static final ResourceLocation FLOWERING_LIT_TEXTURE_1 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit.png");
    private static final ResourceLocation FLOWERING_LIT_TEXTURE_2 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_1.png");
    private static final ResourceLocation FLOWERING_LIT_TEXTURE_3 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_2.png");
    private static final ResourceLocation FLOWERING_LIT_TEXTURE_4 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_3.png");
    private static final ResourceLocation FLOWERING_LIT_TEXTURE_5 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_lit_4.png");

    private static final ResourceLocation FLOWERING_GRUMPY_LIT_TEXTURE_1 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit.png");
    private static final ResourceLocation FLOWERING_GRUMPY_LIT_TEXTURE_2 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_1.png");
    private static final ResourceLocation FLOWERING_GRUMPY_LIT_TEXTURE_3 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_2.png");
    private static final ResourceLocation FLOWERING_GRUMPY_LIT_TEXTURE_4 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_3.png");
    private static final ResourceLocation FLOWERING_GRUMPY_LIT_TEXTURE_5 = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/glare/flowering_glare_grumpy_lit_4.png");

    public static final Pair<ResourceLocation[], ResourceLocation[]> FLOWERING_GRUMPY = new Pair<>(
            new ResourceLocation[]{FLOWERING_GRUMPY_TEXTURE, FLOWERING_GRUMPY_LIT_TEXTURE_1, FLOWERING_GRUMPY_LIT_TEXTURE_2, FLOWERING_GRUMPY_LIT_TEXTURE_3, FLOWERING_GRUMPY_LIT_TEXTURE_4, FLOWERING_GRUMPY_LIT_TEXTURE_5},
            new ResourceLocation[]{FLOWERING_NORMAL_TEXTURE, FLOWERING_LIT_TEXTURE_1, FLOWERING_LIT_TEXTURE_2, FLOWERING_LIT_TEXTURE_3, FLOWERING_LIT_TEXTURE_4, FLOWERING_LIT_TEXTURE_5}
    );

    public static final Pair<ResourceLocation[], ResourceLocation[]> NORMAL_GRUMPY = new Pair<>(
            new ResourceLocation[]{GRUMPY_TEXTURE, GRUMPY_LIT_TEXTURE_1, GRUMPY_LIT_TEXTURE_2, GRUMPY_LIT_TEXTURE_3, GRUMPY_LIT_TEXTURE_4, GRUMPY_LIT_TEXTURE_5},
            new ResourceLocation[]{NORMAL_TEXTURE, LIT_TEXTURE_1, LIT_TEXTURE_2, LIT_TEXTURE_3, LIT_TEXTURE_4, LIT_TEXTURE_5}
    );

    public GlareRenderer(EntityRendererProvider.Context context) {
        super(context, new GlareModel<>(context.bakeLayer(USEntityRenderer.GLARE)), 0.6F);
        this.addLayer(new GlareGlowberriesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(GlareEntity entity) {
        if (entity.isShiny()) {
            return entity.isGrumpy() ? FLOWERING_GRUMPY.getFirst()[entity.getGlowberries()] : FLOWERING_GRUMPY.getSecond()[entity.getGlowberries()];
        } else {
            return entity.isGrumpy() ? NORMAL_GRUMPY.getFirst()[entity.getGlowberries()] : NORMAL_GRUMPY.getSecond()[entity.getGlowberries()];
        }
    }

    @Override
    protected boolean isShaking(GlareEntity entity) {
        return entity.isGrumpy();
    }
}