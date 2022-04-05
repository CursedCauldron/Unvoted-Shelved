package com.cursedcauldron.unvotedandshelved.client.entity.animation;

import com.cursedcauldron.unvotedandshelved.mixin.ModelPartAccessor;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//<>

@Environment(EnvType.CLIENT)
public class Animator {
    public static void run(HierarchicalModel<?> model, Animation animation, long startTime, float speed, Vector3f vec) {
        float timestamp = getTimestamp(animation, startTime);
        for (Map.Entry<String, List<Transformation>> entry : animation.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = getModelParts(model, entry.getKey());
            List<Transformation> transformations = entry.getValue();
            optional.ifPresent(part -> transformations.forEach(transformation -> {
                Keyframe[] keyframes = transformation.keyframes();
                int start = Math.max(0, Mth.binarySearch(0, keyframes.length, index -> timestamp <= keyframes[index].timestamp()) - 1);
                int end = Math.min(keyframes.length - 1, start + 1);
                Keyframe startFrame = keyframes[start];
                Keyframe endFrame = keyframes[end];
                float current = timestamp - startFrame.timestamp();
                float delta = Mth.clamp(current / (endFrame.timestamp() - startFrame.timestamp()), 0.0F, 1.0F);
                endFrame.interpolation().apply(vec, delta, keyframes, start, end, speed);
                transformation.target().apply(part, vec);
            }));
        }
    }

    private static Optional<ModelPart> getModelParts(HierarchicalModel<?> model, String key) {
        return model.root().getAllParts().filter(part -> ((ModelPartAccessor)(Object)part).getChildren().containsKey(key)).findFirst().map(part -> part.getChild(key));
    }

    private static float getTimestamp(Animation animation, long startTime) {
        float timeInMillis = (float)startTime / 1000.0F;
        return animation.looping() ? timeInMillis % animation.lengthInSeconds() : timeInMillis;
    }

    public static Vector3f position(float x, float y, float z) {
        return new Vector3f(x, -y, z);
    }

    public static Vector3f rotation(float x, float y, float z) {
        float f = (float)Math.PI / 180.0F;
        return new Vector3f(x * f, y * f, z * f);
    }

    public static Vector3f scale(double x, double y, double z) {
        return new Vector3f((float)x - 1.0F, (float)y - 1.0F, (float)z - 1.0F);
    }
}