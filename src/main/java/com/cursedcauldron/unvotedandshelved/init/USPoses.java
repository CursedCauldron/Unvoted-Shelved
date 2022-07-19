package com.cursedcauldron.unvotedandshelved.init;

import net.minecraft.world.entity.Pose;

public enum USPoses {
    HEAD_SPIN,
    PRESS_BUTTON,
    PRESS_BUTTON_UP,
    PRESS_BUTTON_DOWN;

    public Pose get() {
        return Pose.valueOf(this.name());
    }
}
