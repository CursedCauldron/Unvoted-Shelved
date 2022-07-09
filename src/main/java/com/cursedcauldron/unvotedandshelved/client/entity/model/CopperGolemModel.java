package com.cursedcauldron.unvotedandshelved.client.entity.model;

import com.cursedcauldron.unvotedandshelved.client.entity.CopperGolemAnimations;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class CopperGolemModel<T extends CopperGolemEntity> extends HierarchicalModel<T> {
    private static final Vector3f ANIMATION_PROGRESS = new Vector3f();
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart root;
    
    public CopperGolemModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        PartDefinition body = part.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -6.0F, 14.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));
        head.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, -16.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(32, 21).addBox(-2.0F, -20.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 4.0F, 0.0F));
        body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(36, 33).mirror().addBox(-3.0F, 0.0F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.5F, 4.0F, 0.0F));
        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-1.5F, -1.0F, -1.5F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.5F, -4.0F, 0.0F));
        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 37).addBox(-2.5F, -1.0F, -1.5F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -4.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float angle, float distance, float animationProgress, float yaw, float pitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = yaw * 0.017453292F;
        float speed = Math.min((float)entity.getDeltaMovement().lengthSqr() * 125.0F, 1.0F);
        this.animate(entity.walkingAnimation, CopperGolemAnimations.walkingAnimation(entity.getStage()), animationProgress, speed);

        this.animate(entity.headSpinAnimation, CopperGolemAnimations.HEAD_SPIN, animationProgress);
        this.animate(entity.headSpinSlowerAnimation, CopperGolemAnimations.HEAD_SPIN_SLOWER, animationProgress);
        this.animate(entity.headSpinSlowestAnimation, CopperGolemAnimations.HEAD_SPIN_SLOWEST, animationProgress);

        this.animate(entity.buttonAnimation, CopperGolemAnimations.BUTTON_PRESS, animationProgress);
        this.animate(entity.buttonSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS, animationProgress);
        this.animate(entity.buttonSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS, animationProgress);

        this.animate(entity.buttonUpAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, animationProgress);
        this.animate(entity.buttonUpSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, animationProgress);
        this.animate(entity.buttonUpSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, animationProgress);

        this.animate(entity.buttonDownAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, animationProgress);
        this.animate(entity.buttonDownSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, animationProgress);
        this.animate(entity.buttonDownSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, animationProgress);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}