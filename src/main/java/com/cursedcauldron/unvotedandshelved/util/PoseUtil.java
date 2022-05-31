package com.cursedcauldron.unvotedandshelved.util;

import com.cursedcauldron.unvotedandshelved.init.USPoses;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;

public class PoseUtil {

    public static <E extends Entity> void setModPose(USPoses poses, E entity) {
        setModPose(poses.name(), entity);
    }

    public static <E extends Entity> void setModPose(String name, E entity) {
        entity.setPose(Pose.valueOf(name));
    }

    public static <E extends Entity> boolean isInPose(E entity, USPoses pose) {
        return entity.getPose().name().equals(pose.name());
    }

}
