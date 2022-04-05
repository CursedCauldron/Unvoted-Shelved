package com.cursedcauldron.unvotedandshelved.client.entity.animation;

import com.cursedcauldron.unvotedandshelved.client.entity.model.IModelAccess;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

//<>

@Environment(EnvType.CLIENT)
public record Transformation(Target target, Keyframe... keyframes) {
    @Environment(EnvType.CLIENT)
    public interface Target {
        void apply(ModelPart part, Vector3f vec);
    }

    @Environment(EnvType.CLIENT)
    public static class Interpolations {
        public static final Interpolation LINEAL = (vec, delta, keyframes, start, end, speed) -> {
            Vector3f startTarget = keyframes[start].target();
            Vector3f endTarget = keyframes[end].target();
            vec.set(Mth.lerp(delta, startTarget.x(), endTarget.x()) * speed, Mth.lerp(delta, startTarget.y(), endTarget.y()) * speed, Mth.lerp(delta, startTarget.z(), endTarget.z()) * speed);
        };
        public static final Interpolation CATMULL = (vec, delta, keyframes, start, end, speed) -> {
            Vector3f firstTarget = keyframes[Math.max(0, start - 1)].target();
            Vector3f secondTarget = keyframes[start].target();
            Vector3f thirdTarget = keyframes[end].target();
            Vector3f fourthTarget = keyframes[Math.min(keyframes.length - 1, end + 1)].target();
            vec.set(catmull(delta, firstTarget.x(), secondTarget.x(), thirdTarget.x(), fourthTarget.x()) * speed, catmull(delta, firstTarget.y(), secondTarget.y(), thirdTarget.y(), fourthTarget.y()) * speed,catmull(delta, firstTarget.z(), secondTarget.z(), thirdTarget.z(), fourthTarget.z()) * speed);
        };
    }

    public static float catmull(float delta, float first, float second, float third, float fourth) {
        return 0.5F * (2.0F * second + (third - first) * delta + (2.0F * first - 5.0F * second + 4.0F * third - fourth) * delta * delta + (3.0F * second - first - 3.0F * third + fourth) * delta * delta * delta);
    }

    @Environment(EnvType.CLIENT)
    public static class Targets {
        public static final Target TRANSLATE    = IModelAccess::translate;
        public static final Target ROTATE       = IModelAccess::rotate;
        public static final Target SCALE        = IModelAccess::scale;
    }

    @Environment(EnvType.CLIENT)
    public interface Interpolation {
        void apply(Vector3f vec, float delta, Keyframe[] keyframes, int start, int end, float speed);
    }
}