package com.cursedcauldron.unvotedandshelved.client.entity.model;

import com.cursedcauldron.unvotedandshelved.client.entity.CopperGolemAnimations;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.Animation;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationHelper;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationState;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.FrozenCopperGolemEntity;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

//<>

@Environment(EnvType.CLIENT)
public class FrozenCopperGolemModel<T extends FrozenCopperGolemEntity> extends HierarchicalModel<T> {
    private static final Vector3f ANIMATION_PROGRESS = new Vector3f();
    private final ModelPart head;
    private final ModelPart body;

    public FrozenCopperGolemModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
    }



    public static LayerDefinition getLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -6.0F, 14.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));
        head.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, -16.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(32, 21).addBox(-2.0F, -20.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 4.0F, 0.0F));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(36, 33).mirror().addBox(-3.0F, 0.0F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.5F, 4.0F, 0.0F));
        body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-1.5F, -1.0F, -1.5F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.5F, -4.0F, 0.0F));
        body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 37).addBox(-2.5F, -1.0F, -1.5F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -4.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float tickDelta, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelModifier::resetPose);
    }

    private void runAnimation(AnimationState animationState, Animation animation, long time) {
        animationState.run(state -> AnimationHelper.animate(this, animation, time - state.getStartTime(), 1.0F, ANIMATION_PROGRESS));
    }

    @Override
    public ModelPart root() {
        return this.body;
    }


}