package com.cursedcauldron.unvotedandshelved.util;

import com.cursedcauldron.unvotedandshelved.client.entity.models.ModelModifier;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record Transformation(Target target, Keyframe... keyframes) {
    @OnlyIn(Dist.CLIENT)
    public interface Target {
        void apply(ModelPart part, Vector3f vec);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Interpolations {
        public static final Interpolation LINEAL = (animationProgress, delta, keyframes, start, end, speed) -> {
            Vector3f startTarget = keyframes[start].target();
            Vector3f endTarget = keyframes[end].target();
            animationProgress.set(Mth.lerp(delta, startTarget.x(), endTarget.x()) * speed, Mth.lerp(delta, startTarget.y(), endTarget.y()) * speed, Mth.lerp(delta, startTarget.z(), endTarget.z()) * speed);
        };
        public static final Interpolation CATMULLROM = (animationProgress, delta, keyframes, start, end, speed) -> {
            Vector3f firstTarget = keyframes[Math.max(0, start - 1)].target();
            Vector3f secondTarget = keyframes[start].target();
            Vector3f thirdTarget = keyframes[end].target();
            Vector3f fourthTarget = keyframes[Math.min(keyframes.length - 1, end + 1)].target();
            animationProgress.set(catmullrom(delta, firstTarget.x(), secondTarget.x(), thirdTarget.x(), fourthTarget.x()) * speed, catmullrom(delta, firstTarget.y(), secondTarget.y(), thirdTarget.y(), fourthTarget.y()) * speed, catmullrom(delta, firstTarget.z(), secondTarget.z(), thirdTarget.z(), fourthTarget.z()) * speed);
        };
    }

    public static float catmullrom(float delta, float p0, float p1, float p2, float p3) {
        return 0.5F * (2.0F * p1 + (p2 - p0) * delta + (2.0F * p0 - 5.0F * p1 + 4.0F * p2 - p3) * delta * delta + (3.0F * p1 - p0 - 3.0F * p2 + p3) * delta * delta * delta);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Targets {
        public static final Target TRANSLATE = ModelModifier::translate;
        public static final Target ROTATE = ModelModifier::rotate;
        public static final Target SCALE = ModelModifier::scale;
    }

    @OnlyIn(Dist.CLIENT)
    public interface Interpolation {
        void apply(Vector3f animationProgress, float delta, Keyframe[] keyframes, int start, int end, float speed);
    }
}
