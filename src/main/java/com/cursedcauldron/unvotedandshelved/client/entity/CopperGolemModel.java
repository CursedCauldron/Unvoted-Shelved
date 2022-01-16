package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import software.bernie.example.entity.GeoExampleEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Collections;

public class CopperGolemModel extends AnimatedTickingGeoModel<CopperGolemEntity> {
    private static final Identifier[] TEXTURES = new Identifier[]{
            UnvotedAndShelved.ID("textures/entity/copper_golem/copper_golem.png"),
            UnvotedAndShelved.ID("textures/entity/copper_golem/exposed_copper_golem.png"),
            UnvotedAndShelved.ID("textures/entity/copper_golem/weathered_copper_golem.png"),
            UnvotedAndShelved.ID("textures/entity/copper_golem/oxidized_copper_golem.png")
    };

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
        //Prevent Crash
        return entity.getOxidationStage() > 3 ? TEXTURES[0] : TEXTURES[entity.getOxidationStage()];
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
