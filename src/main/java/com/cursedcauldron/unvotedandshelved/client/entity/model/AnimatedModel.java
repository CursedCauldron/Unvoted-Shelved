package com.cursedcauldron.unvotedandshelved.client.entity.model;

import com.cursedcauldron.unvotedandshelved.client.entity.animation.Animation;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationHelper;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationState;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

//<>

@Environment(EnvType.CLIENT)
public abstract class AnimatedModel<E extends Entity> extends HierarchicalModel<E> {
    private static final Vector3f ANIMATION_PROGRESS = new Vector3f();

    public AnimatedModel() {
        this(RenderType::entityCutoutNoCull);
    }

    public AnimatedModel(Function<ResourceLocation, RenderType> function) {
        super(function);
    }

    protected void runAnimation(AnimationState animationState, Animation animation) {
        this.runAnimation(animationState, animation, 1.0F);
    }

    protected void runAnimation(AnimationState animationState, Animation animation, float time) {
        animationState.run(Minecraft.getInstance().isPaused(), time);
        animationState.run(state -> AnimationHelper.animate(this, animation, state.runningTime(), 1.0F, ANIMATION_PROGRESS));
    }
}