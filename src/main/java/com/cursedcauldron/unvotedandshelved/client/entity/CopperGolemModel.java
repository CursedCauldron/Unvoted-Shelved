package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CopperGolemModel extends AnimatedTickingGeoModel<CopperGolemEntity> {
    @Override
    public Identifier getAnimationFileLocation(CopperGolemEntity entity) {
        return new Identifier(UnvotedAndShelved.MODID, "animations/coppergolem.animation.json");
    }

    @Override
    public Identifier getModelLocation(CopperGolemEntity entity) {
        return new Identifier(UnvotedAndShelved.MODID, "geo/copper_golem.geo.json");
    }

    @Override
    public Identifier getTextureLocation(CopperGolemEntity entity) {
        return new Identifier(UnvotedAndShelved.MODID, "textures/entity/copper_golem/copper_golem.png");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(CopperGolemEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null && !customPredicate.isMoving()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 360F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 999F));
        }
    }
}
