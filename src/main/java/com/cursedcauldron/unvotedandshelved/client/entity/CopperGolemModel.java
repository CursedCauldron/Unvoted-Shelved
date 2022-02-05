package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.Maps;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.Map;

public class CopperGolemModel extends AnimatedTickingGeoModel<CopperGolemEntity> {
    private static final Map<CopperGolemEntity.OxidationStage, Identifier> TEXTURES = Util.make(Maps.newHashMap(), (oxidateionStages) -> {
        CopperGolemEntity.OxidationStage[] stages = CopperGolemEntity.OxidationStage.OXIDATION_STAGES;
        for (CopperGolemEntity.OxidationStage stage : stages) {
            oxidateionStages.put(stage, new Identifier(UnvotedAndShelved.MODID, String.format("textures/entity/copper_golem/%s_copper_golem.png", stage.getName())));
        }
    });

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
        return TEXTURES.get(entity.getOxidationStage());
    }

    @Override
    public void setLivingAnimations(CopperGolemEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null && !customPredicate.isMoving()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

}
