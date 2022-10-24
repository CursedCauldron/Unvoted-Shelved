package com.cursedcauldron.unvotedandshelved.client.entity.model;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class GlareModel<T extends GlareEntity> extends HierarchicalModel<T> {
    private final ModelPart body;
    private final ModelPart frown;
    private final ModelPart leaves;
    private final ModelPart bottomLeaves;

    public GlareModel(ModelPart root) {
        this.body = root.getChild("body");
        this.frown = this.body.getChild("frown");
        this.leaves = this.body.getChild("leaves");
        this.bottomLeaves = this.leaves.getChild("bottomLeaves");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition data = new MeshDefinition();
        PartDefinition root = data.getRoot();

        PartDefinition body = root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror(false)
                        .addBox(-7.0F, -13.0F, -1.0F, 14.0F, 13.0F, 8.0F)
                        .texOffs(0, 21)
                        .mirror(false)
                        .addBox(-7.0F, -13.0F, -7.0F, 14.0F, 13.0F, 6.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F)
        );
        
        PartDefinition frown = body.addOrReplaceChild(
                "frown",
                CubeListBuilder.create()
                        .texOffs(52, 8)
                        .addBox(-7.0F, -13.0F, -1.1F, 14.0F, 13.0F, 0.0F),
                PartPose.ZERO
        );

        PartDefinition leaves = body.addOrReplaceChild(
                "leaves",
                CubeListBuilder.create()
                        .texOffs(0, 40)
                        .mirror(false)
                        .addBox(-6.0F, 0.0F, -5.0F, 12.0F, 7.0F, 10.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        PartDefinition bottomLeaves = leaves.addOrReplaceChild(
                "bottomLeaves",
                CubeListBuilder.create()
                        .texOffs(44, 48)
                        .mirror(false)
                        .addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(data, 80, 64);
    }

    @Override
    public ModelPart root() {
        return this.body;
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        boolean isGrumpy = entity.isGrumpy();
        float speed = isGrumpy ? 1.25F : 1.0F;
        float degree = 2.0F;
        this.frown.visible = isGrumpy;

        if (!isGrumpy) {
            this.body.y = Mth.cos(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance + 17;
            this.leaves.z = Mth.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.leaves.xRot = Mth.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.leaves.y = Mth.cos(limbDistance - 0.05F) - 2;
            this.body.xRot = Mth.cos(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance;
            this.body.zRot = Mth.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.2F * limbDistance;
            this.bottomLeaves.z = Mth.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.bottomLeaves.xRot = Mth.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.bottomLeaves.y = Mth.cos(limbDistance - 0.05F);
        } else {
            this.body.y = Mth.cos(limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 17;
            this.leaves.z = Mth.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.leaves.xRot = Mth.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.leaves.y = Mth.cos(limbDistance - 0.05F) - 2;
            this.body.xRot = Mth.cos(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance;
            this.body.zRot = Mth.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.2F * limbDistance;
            this.bottomLeaves.z = Mth.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.bottomLeaves.xRot = Mth.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.bottomLeaves.y = Mth.cos(limbDistance - 0.05F);
            this.body.x = Mth.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.6F * limbDistance;
        }
    }
}