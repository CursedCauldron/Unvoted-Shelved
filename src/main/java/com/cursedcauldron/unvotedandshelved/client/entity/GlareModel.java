package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

//<>

public class GlareModel<T extends Entity> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer leaves;
    private final ModelRenderer bottomLeaves;

    public GlareModel() {
        this.textureWidth = 80;
        this.textureHeight = 64;

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-7.0F, -13.0F, -1.0F, 14.0F, 13.0F, 8.0F, 0.0F, false);
        this.body.setTextureOffset(0, 21).addBox(-7.0F, -13.0F, -7.0F, 14.0F, 13.0F, 6.0F, 0.0F, false);

        this.leaves = new ModelRenderer(this);
        this.leaves.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addChild(this.leaves);
        this.leaves.setTextureOffset(0, 40).addBox(-6.0F, 0.0F, -5.0F, 12.0F, 7.0F, 10.0F, 0.0F, false);

        this.bottomLeaves = new ModelRenderer(this);
        this.bottomLeaves.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leaves.addChild(this.bottomLeaves);
        this.bottomLeaves.setTextureOffset(44, 48).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean isGrumpy = ((GlareEntity)entityIn).isGrumpy();
        float speed;
        float degree = 2.0F;

        if (!isGrumpy) {
            speed = 1.0F;
            this.body.rotationPointY = MathHelper.cos(limbSwing * speed * 0.3F) * degree * 0.2F* limbSwingAmount + 17;
            this.leaves.rotationPointZ = MathHelper.cos(-1.0F + limbSwing * speed * 0.3F) * degree * 0.1F * limbSwingAmount + 0.05F;
            this.leaves.rotateAngleX = MathHelper.cos(-1.0F + limbSwing * speed * 0.3F) * degree * 0.3F * limbSwingAmount + 0.1F;
            this.leaves.rotationPointY = MathHelper.cos(limbSwingAmount - 0.05F) - 2;
            this.body.rotateAngleX = MathHelper.cos(limbSwing * speed * 0.3F) * degree * 0.2F * limbSwingAmount;
            this.body.rotateAngleZ = MathHelper.cos(-1.0F + limbSwing * speed * 0.15F) * degree * 0.2F * limbSwingAmount;
            this.bottomLeaves.rotationPointZ = MathHelper.cos(-2.0F + limbSwing * speed * 0.3F) * degree * 0.1F * limbSwingAmount + 0.05F;
            this.bottomLeaves.rotateAngleX = MathHelper.cos(-2.0F + limbSwing * speed * 0.3F) * degree * 0.3F * limbSwingAmount + 0.1F;
            this.bottomLeaves.rotationPointY = MathHelper.cos(limbSwingAmount - 0.05F);
        } else {
            speed = 1.25F;
            this.body.rotationPointY = MathHelper.cos(limbSwing * speed * 0.3F) * degree * 0.3F * limbSwingAmount + 17;
            this.leaves.rotationPointZ = MathHelper.cos(-1.0F + limbSwing * speed * 0.3F) * degree * 0.1F * limbSwingAmount + 0.05F;
            this.leaves.rotateAngleX = MathHelper.cos(-1.0F + limbSwing * speed * 0.3F) * degree * 0.3F * limbSwingAmount + 0.1F;
            this.leaves.rotationPointY = MathHelper.cos(limbSwingAmount - 0.05F) - 2;
            this.body.rotateAngleX = MathHelper.cos(limbSwing * speed * 0.3F) * degree * 0.2F * limbSwingAmount;
            this.body.rotateAngleZ = MathHelper.cos(-1.0F + limbSwing * speed * 0.15F) * degree * 0.2F * limbSwingAmount;
            this.bottomLeaves.rotationPointZ = MathHelper.cos(-2.0F + limbSwing * speed * 0.3F) * degree * 0.1F * limbSwingAmount + 0.05F;
            this.bottomLeaves.rotateAngleX = MathHelper.cos(-2.0F + limbSwing * speed * 0.3F) * degree * 0.3F * limbSwingAmount + 0.1F;
            this.bottomLeaves.rotationPointY = MathHelper.cos(limbSwingAmount - 0.05F);
            this.body.rotationPointX = MathHelper.cos(-1.0F + limbSwing * speed * 0.15F) * degree * 0.6F * limbSwingAmount;
        }

    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }
}