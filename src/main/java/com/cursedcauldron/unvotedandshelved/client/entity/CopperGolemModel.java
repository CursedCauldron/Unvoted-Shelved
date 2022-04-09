package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class CopperGolemModel<T extends CopperGolemEntity> extends HierarchicalModel<T> {
    private final ModelPart root;

    public CopperGolemModel(ModelPart part) {
        this.root = part;
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition data = new MeshDefinition();
        PartDefinition partData = data.getRoot();

        PartDefinition body = partData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 15.0F, 0.0F));


        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -6.0F, 14.0F, 9.0F, 12.0F).texOffs(0, 21).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 3.0F, 2.0F).texOffs(32, 21).addBox(-2.0F, -16.0F, -2.0F, 4.0F, 4.0F, 4.0F).texOffs(0, 0).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 6.0F, 2.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offset(-7.5F, -4.0F, 0.0F));
        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F).mirror(false), PartPose.offset(7.5F, -4.0F, 0.0F));
        body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(-3.0F, 4.0F, 0.0F));
        body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(36, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F).mirror(false), PartPose.offset(3.0F, 4.0F, 0.0F));

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
