package com.cursedcauldron.unvotedandshelved.client.entity.animation;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

//<>

@Environment(EnvType.CLIENT)
public record Animation(float lengthInSeconds, boolean looping, Map<String, List<Transformation>> boneAnimations) {
    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final float lengthInSeconds;
        private final Map<String, List<Transformation>> transformations = Maps.newHashMap();
        private boolean looping;

        public static Builder create(float lengthInSeconds) {
            return new Builder(lengthInSeconds);
        }

        public Builder(float lengthInSeconds) {
            this.lengthInSeconds = lengthInSeconds;
        }

        public Builder looping() {
            this.looping = true;
            return this;
        }

        public Builder addBoneAnimation(String key, Transformation transformation) {
            this.transformations.computeIfAbsent(key, name -> Lists.newArrayList()).add(transformation);
            return this;
        }

        public Animation build() {
            return new Animation(this.lengthInSeconds, this.looping, this.transformations);
        }
    }
}