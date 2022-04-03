package com.cursedcauldron.unvotedandshelved.client.entity.render.feature;

import com.cursedcauldron.unvotedandshelved.client.entity.models.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.init.USModelLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CopperGolemPowerLayer extends EnergySwirlLayer<CopperGolemEntity, CopperGolemModel<CopperGolemEntity>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final CopperGolemModel<CopperGolemEntity> model;

    public CopperGolemPowerLayer(RenderLayerParent<CopperGolemEntity, CopperGolemModel<CopperGolemEntity>> parent, EntityModelSet context) {
        super(parent);
        this.model = new CopperGolemModel<>(context.bakeLayer(USModelLayers.COPPER_GOLEM_ARMOR));
    }

    @Override
    protected float xOffset(float p_116968_) {
        return p_116968_ * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected EntityModel<CopperGolemEntity> model() {
        return this.model;
    }
}
