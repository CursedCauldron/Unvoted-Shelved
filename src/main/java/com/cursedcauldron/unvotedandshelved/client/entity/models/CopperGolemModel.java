package com.cursedcauldron.unvotedandshelved.client.entity.models;

import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//<>

@OnlyIn(Dist.CLIENT)
public class CopperGolemModel<T extends CopperGolemEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public CopperGolemModel(ModelPart part) {
        this.root = part;
        this.head = part.getChild("head");
        this.rightArm = part.getChild("right_arm");
        this.leftArm = part.getChild("left_arm");
        this.rightLeg = part.getChild("right_leg");
        this.leftLeg = part.getChild("left_leg");
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition data = new MeshDefinition();
        PartDefinition partData = data.getRoot();

        PartDefinition body = partData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -6.0F, 14.0F, 9.0F, 12.0F).texOffs(0, 21).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 3.0F, 2.0F).texOffs(32, 21).addBox(-2.0F, -16.0F, -2.0F, 4.0F, 4.0F, 4.0F).texOffs(0, 0).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 6.0F, 2.0F), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offset(-7.5F, -4.0F, 0.0F));
        PartDefinition leftArm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F).mirror(false), PartPose.offset(7.5F, -4.0F, 0.0F));
        PartDefinition rightLeg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(-3.0F, 4.0F, 0.0F));
        PartDefinition leftLeg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F).mirror(false), PartPose.offset(3.0F, 4.0F, 0.0F));

        partData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -6.0F, 14.0F, 9.0F, 12.0F).texOffs(0, 21).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 3.0F, 2.0F).texOffs(32, 21).addBox(-2.0F, -16.0F, -2.0F, 4.0F, 4.0F, 4.0F).texOffs(0, 0).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 6.0F, 2.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
        partData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 15.0F, 0.0F));
        partData.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offset(-7.5F, -4.0F, 0.0F));
        partData.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F).mirror(false), PartPose.offset(7.5F, -4.0F, 0.0F));
        partData.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(-3.0F, 4.0F, 0.0F));
        partData.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F).mirror(false), PartPose.offset(3.0F, 4.0F, 0.0F));
        return LayerDefinition.create(data, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float p_102621_, float p_102622_, float p_102623_) {

    }
}