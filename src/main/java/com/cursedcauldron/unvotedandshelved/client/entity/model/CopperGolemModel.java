package com.cursedcauldron.unvotedandshelved.client.entity.model;

import com.cursedcauldron.unvotedandshelved.client.entity.CopperGolemAnimations;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.Animation;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationHelper;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationState;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

//<>

@Environment(EnvType.CLIENT)
public class CopperGolemModel<T extends CopperGolemEntity> extends AnimatedModel<T> {
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
        this.root().getAllParts().forEach(ModelModifier::resetPose);
        long time = Util.getMillis();
        this.head.yRot = yaw * 0.017453292F;
        float walkTime = Math.min((float)entity.getDeltaMovement().lengthSqr() * 200.0F, 8.0F);
        this.runAnimation(entity.walkingAnimation, CopperGolemAnimations.walkingAnimation(entity.getStage()), walkTime);

        this.runAnimation(entity.headSpinAnimation, CopperGolemAnimations.HEAD_SPIN, time);
        this.runAnimation(entity.headSpinSlowerAnimation, CopperGolemAnimations.HEAD_SPIN_SLOWER, time);
        this.runAnimation(entity.headSpinSlowestAnimation, CopperGolemAnimations.HEAD_SPIN_SLOWEST, time);

        this.runAnimation(entity.buttonAnimation, CopperGolemAnimations.BUTTON_PRESS, time);
        this.runSlowerAnimation(entity.buttonSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS, time);
        this.runSlowestAnimation(entity.buttonSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS, time);

        this.runAnimation(entity.buttonUpAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, time);
        this.runSlowerAnimation(entity.buttonUpSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, time);
        this.runSlowestAnimation(entity.buttonUpSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, time);

        this.runAnimation(entity.buttonDownAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, time);
        this.runSlowerAnimation(entity.buttonDownSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, time);
        this.runSlowestAnimation(entity.buttonDownSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, time);
    }


    private void runAnimation(AnimationState animationState, Animation animation, long time) {
        animationState.run(Minecraft.getInstance().isPaused(), time);
        animationState.run(state -> AnimationHelper.animate(this, animation, time - state.getStartTime(), 1.0F, ANIMATION_PROGRESS));
    }

    private void runSlowerAnimation(AnimationState animationState, Animation animation, long time) {
        animationState.run(Minecraft.getInstance().isPaused(), time);
        animationState.run(state -> AnimationHelper.animate(this, animation, time - state.getStartTime(), 0.75F, ANIMATION_PROGRESS));
    }

    private void runSlowestAnimation(AnimationState animationState, Animation animation, long time) {
        animationState.run(Minecraft.getInstance().isPaused(), time);
        animationState.run(state -> AnimationHelper.animate(this, animation, time - state.getStartTime(), 0.5F, ANIMATION_PROGRESS));
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}