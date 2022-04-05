package com.cursedcauldron.unvotedandshelved.client.entity.animation;

import net.minecraft.Util;

import java.util.function.Consumer;

//<>

public class AnimationState {
    private long startedAt = Long.MAX_VALUE;

    public void start() {
        this.startedAt = Util.getMillis();
    }

    public void startIfNotRunning() {
        if (!this.isRunning()) {
            this.start();
        }
    }

    public void stop() {
        this.startedAt = Long.MAX_VALUE;
    }

    public long getStartTime() {
        return this.startedAt;
    }

    public void run(Consumer<AnimationState> consumer) {
        if (this.isRunning()) {
            consumer.accept(this);
        }
    }

    private boolean isRunning() {
        return this.startedAt != Long.MAX_VALUE;
    }
}