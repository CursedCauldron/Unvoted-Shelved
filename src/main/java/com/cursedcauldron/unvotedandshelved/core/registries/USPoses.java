package com.cursedcauldron.unvotedandshelved.core.registries;

import net.minecraft.world.entity.Pose;

// Pose Registry

public enum USPoses {
    HEAD_SPIN,
    PRESS_BUTTON,
    PRESS_BUTTON_UP,
    PRESS_BUTTON_DOWN;

    public Pose get() {
        return Pose.valueOf(this.name());
    }
}