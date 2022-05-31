package com.cursedcauldron.unvotedandshelved.util;

import net.minecraft.Util;

import java.util.function.Consumer;

public class AnimationState {
    private long startedAt = Long.MAX_VALUE;
    private long runningTime;

    public void start() {
        this.startedAt = Util.getMillis();
        this.runningTime = 0L;
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

    public void run(boolean isPaused, float time) {
        if (this.isRunning()) {
            long millis = Util.getMillis();
            if (!isPaused) {
                this.runningTime += (long)((float)(millis - this.startedAt) * time);
            }
            this.startedAt = millis;
        }
    }

    public long runningTime() {
        return this.runningTime;
    }

    private boolean isRunning() {
        return this.startedAt != Long.MAX_VALUE;
    }
}
