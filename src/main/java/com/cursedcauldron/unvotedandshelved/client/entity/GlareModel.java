package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

//<>

public class GlareModel<T extends Entity> extends SinglePartEntityModel<T> {
    private final ModelPart body;
    private final ModelPart leaves;
    private final ModelPart bottomLeaves;

    public GlareModel(ModelPart root) {
        this.body = root.getChild("body");
        this.leaves = this.body.getChild("leaves");
        this.bottomLeaves = this.leaves.getChild("bottomLeaves");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild(
                "body",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .mirrored(false)
                        .cuboid(-7.0F, -13.0F, -1.0F, 14.0F, 13.0F, 16.0F, new Dilation(0.0F))
                        .uv(0, 21)
                        .mirrored(false)
                        .cuboid(-7.0F, -13.0F, -7.0F, 14.0F, 13.0F, 12.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 17.0F, 0.0F)
        );

        ModelPartData leaves = body.addChild(
                "leaves",
                ModelPartBuilder.create()
                        .uv(0, 40)
                        .mirrored(false)
                        .cuboid(-6.0F, 0.0F, -5.0F, 12.0F, 7.0F, 20.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
        );

        ModelPartData bottomLeaves = leaves.addChild(
                "bottomLeaves",
                ModelPartBuilder.create()
                        .uv(44, 48)
                        .mirrored(false)
                        .cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 12.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
        );

        return TexturedModelData.of(data, 80, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.body;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        boolean isGrumpy = ((GlareEntity)entity).isGrumpy();
        float speed;
        float degree = 2.0F;

        if (!isGrumpy) {
            speed = 1.0F;
            this.body.pivotY = MathHelper.cos(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance + 17;
            this.leaves.pivotZ = MathHelper.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.leaves.pitch = MathHelper.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.leaves.pivotY = MathHelper.cos(limbDistance - 0.05F) - 2;
            this.body.pitch = MathHelper.cos(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance;
            this.body.roll = MathHelper.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.2F * limbDistance;
            this.bottomLeaves.pivotZ = MathHelper.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.bottomLeaves.pitch = MathHelper.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.bottomLeaves.pivotY = MathHelper.cos(limbDistance - 0.05F);
        } else {
            speed = 1.25F;
            this.body.pivotY = MathHelper.cos(limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 17;
            this.leaves.pivotZ = MathHelper.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.leaves.pitch = MathHelper.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.leaves.pivotY = MathHelper.cos(limbDistance - 0.05F) - 2;
            this.body.pitch = MathHelper.cos(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance;
            this.body.roll = MathHelper.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.2F * limbDistance;
            this.bottomLeaves.pivotZ = MathHelper.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
            this.bottomLeaves.pitch = MathHelper.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
            this.bottomLeaves.pivotY = MathHelper.cos(limbDistance - 0.05F);
            this.body.pivotX = MathHelper.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.6F * limbDistance;
        }
    }
}