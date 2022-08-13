package com.cursedcauldron.unvotedandshelved.client.entity.render.feature;

import com.cursedcauldron.unvotedandshelved.common.entity.MoobloomEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Environment(value=EnvType.CLIENT)
public class MoobloomFlowerLayer<T extends MoobloomEntity> extends RenderLayer<T, CowModel<T>> {
    private final BlockRenderDispatcher blockRenderer;

    public MoobloomFlowerLayer(RenderLayerParent<T, CowModel<T>> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
        super(renderLayerParent);
        this.blockRenderer = blockRenderDispatcher;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T moobloom, float f, float g, float h, float j, float k, float l) {
        boolean bl = Minecraft.getInstance().shouldEntityAppearGlowing(moobloom) && moobloom.isInvisible();
        if (moobloom.isBaby()) {
            return;
        }
        if (moobloom.isInvisible() && !bl) {
            return;
        }
        if (moobloom.getMoobloomType().getItem() == null) return;
        BlockState blockState = Block.byItem(moobloom.getMoobloomType().getItem()).defaultBlockState();
        int m = LivingEntityRenderer.getOverlayCoords(moobloom, 0.0f);
        BakedModel bakedModel = this.blockRenderer.getBlockModel(blockState);
        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0f));
        float size = 1.0f;
        poseStack.scale(-size, -size, size);
        poseStack.translate(-0.5, -0.5, -0.5);
        this.renderMushroomBlock(poseStack, multiBufferSource, i, bl, blockState, m, bakedModel);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(42.0f));
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0f));
        poseStack.scale(-size, -size, size);
        poseStack.translate(-0.5, -0.5, -0.5);
        this.renderMushroomBlock(poseStack, multiBufferSource, i, bl, blockState, m, bakedModel);
        poseStack.popPose();
        poseStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-78.0f));
        poseStack.scale(-size, -size, size);
        poseStack.translate(-0.5, -0.5, -0.5);
        this.renderMushroomBlock(poseStack, multiBufferSource, i, bl, blockState, m, bakedModel);
        poseStack.popPose();
    }

    private void renderMushroomBlock(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, boolean bl, BlockState blockState, int j, BakedModel bakedModel) {
        if (bl) {
            this.blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), blockState, bakedModel, 0.0f, 0.0f, 0.0f, i, j);
        } else {
            this.blockRenderer.renderSingleBlock(blockState, poseStack, multiBufferSource, i, j);
        }
    }
}

