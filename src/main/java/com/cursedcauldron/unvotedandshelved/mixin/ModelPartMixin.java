package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.client.entity.model.ModelModifier;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPart.class)
public class ModelPartMixin implements ModelModifier {
    private float xScale = 1.0F;
    private float yScale = 1.0F;
    private float zScale = 1.0F;
    private PartPose defaultPose = PartPose.ZERO;

    @Override
    public PartPose getDefaultPose() {
        return this.defaultPose;
    }

    @Override
    public void setDefaultPose(PartPose pose) {
        this.defaultPose = pose;
    }

    @Inject(method = "loadPose", at = @At("TAIL"))
    private void US$loadScale(PartPose pose, CallbackInfo ci) {
        this.xScale = 1.0F;
        this.yScale = 1.0F;
        this.zScale = 1.0F;
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void US$copyScale(ModelPart part, CallbackInfo ci) {
        this.setXScale(((ModelModifier)(Object)part).xScale());
        this.setYScale(((ModelModifier)(Object)part).yScale());
        this.setZScale(((ModelModifier)(Object)part).zScale());
    }

    @Inject(method = "translateAndRotate", at = @At("TAIL"))
    private void US$rotateScale(PoseStack stack, CallbackInfo ci) {
        if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
            stack.scale(this.xScale, this.yScale, this.zScale);
        }
    }

    @Override
    public float xScale() {
        return this.xScale;
    }

    @Override
    public void setXScale(float x) {
        this.xScale = x;
    }

    @Override
    public void increaseXScale(float x) {
        this.xScale += x;
    }

    @Override
    public float yScale() {
        return this.yScale;
    }

    @Override
    public void setYScale(float y) {
        this.yScale = y;
    }

    @Override
    public void increaseYScale(float y) {
        this.yScale += y;
    }

    @Override
    public float zScale() {
        return this.zScale;
    }

    @Override
    public void setZScale(float z) {
        this.zScale = z;
    }

    @Override
    public void increaseZScale(float z) {
        this.zScale += z;
    }
}