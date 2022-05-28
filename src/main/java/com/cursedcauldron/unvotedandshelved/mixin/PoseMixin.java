package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.core.registries.USPoses;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(Pose.class)
public class PoseMixin {

    @Invoker("<init>")
    public static Pose newPose(String name, int id) {
        throw new AssertionError();
    }

    @Shadow
    @Mutable
    @Final
    private static Pose[] $VALUES;

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Pose;$VALUES:[Lnet/minecraft/world/entity/Pose;", shift = At.Shift.AFTER))
    private static void US$addCustomPose(CallbackInfo ci) {
        List<Pose> poses = new ArrayList<>(Arrays.asList($VALUES));
        Pose last = poses.get(poses.size() - 1);
        int i = 1;
        for (USPoses pose : USPoses.values()) {
            poses.add(newPose(pose.name(), last.ordinal() + i));
            i++;
        }
        $VALUES = poses.toArray(new Pose[0]);
    }

}
