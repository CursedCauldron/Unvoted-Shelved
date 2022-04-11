package com.cursedcauldron.unvotedandshelved.client.entity;

import com.cursedcauldron.unvotedandshelved.client.entity.animation.Animation;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.AnimationHelper;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.Keyframe;
import com.cursedcauldron.unvotedandshelved.client.entity.animation.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//<>

@Environment(EnvType.CLIENT)
public class CopperGolemAnimations {
    public static final Animation WALKING = Animation.Builder.create(1.08333F)
            .addBoneAnimation("head", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5F, AnimationHelper.rotation(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7917F, AnimationHelper.rotation(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0F, AnimationHelper.rotation(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("antenna", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(0.0F, 45.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5833F, AnimationHelper.rotation(0.0F, 45.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7917F, AnimationHelper.rotation(0.0F, 90.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("antenna", new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0417F, AnimationHelper.translate(0.0F, -0.3F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, AnimationHelper.translate(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0938F, AnimationHelper.translate(0.0F, -0.7F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1042F, AnimationHelper.translate(0.0F, -0.5F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1146F, AnimationHelper.translate(0.0F, -0.5F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, AnimationHelper.translate(0.0F, -0.5F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1354F, AnimationHelper.translate(0.0F, -0.7F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1458F, AnimationHelper.translate(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1563F, AnimationHelper.translate(0.0F, -0.8F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1667F, AnimationHelper.translate(0.0F, -0.7F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.1771F, AnimationHelper.translate(0.0F, -0.8F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1875F, AnimationHelper.translate(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1979F, AnimationHelper.translate(0.0F, -0.8F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, AnimationHelper.translate(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5833F, AnimationHelper.translate(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.translate(0.0F, -0.7F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.6771F, AnimationHelper.translate(0.0F, -0.1F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.6979F, AnimationHelper.translate(0.0F, -0.2F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7188F, AnimationHelper.translate(0.0F, -0.1F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7292F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0833F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("body", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1042F, AnimationHelper.rotation(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, AnimationHelper.rotation(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.3542F, AnimationHelper.rotation(-6.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, AnimationHelper.rotation(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.4479F, AnimationHelper.rotation(1.3F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.4792F, AnimationHelper.rotation(5.3F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5F, AnimationHelper.rotation(7.6F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, AnimationHelper.rotation(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, AnimationHelper.rotation(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.8854F, AnimationHelper.rotation(-8.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0F, AnimationHelper.rotation(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM)
                    ))
            .addBoneAnimation("rightLeg", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0938F, AnimationHelper.rotation(1.9F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, AnimationHelper.rotation(17.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1771F, AnimationHelper.rotation(20.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, AnimationHelper.rotation(35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.375F, AnimationHelper.rotation(35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.4375F, AnimationHelper.rotation(29.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, AnimationHelper.rotation(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5521F, AnimationHelper.rotation(8.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.6667F, AnimationHelper.rotation(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, AnimationHelper.rotation(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7604F, AnimationHelper.rotation(-21.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, AnimationHelper.rotation(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.8542F, AnimationHelper.rotation(-52.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.9271F, AnimationHelper.rotation(-39.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, AnimationHelper.rotation(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0417F, AnimationHelper.rotation(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0625F, AnimationHelper.rotation(7.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("rightLeg", new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.translate(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7917F, AnimationHelper.translate(0.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0833F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("leftLeg", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0938F, AnimationHelper.rotation(-1.9F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, AnimationHelper.rotation(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1771F, AnimationHelper.rotation(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, AnimationHelper.rotation(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.375F, AnimationHelper.rotation(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.4375F, AnimationHelper.rotation(-29.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, AnimationHelper.rotation(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5521F, AnimationHelper.rotation(-8.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.6667F, AnimationHelper.rotation(2.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, AnimationHelper.rotation(17.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7604F, AnimationHelper.rotation(21.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, AnimationHelper.rotation(52.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.8542F, AnimationHelper.rotation(52.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.9271F, AnimationHelper.rotation(39.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, AnimationHelper.rotation(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0417F, AnimationHelper.rotation(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0625F, AnimationHelper.rotation(-7.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("leftLeg", new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.translate(0.0F, 0.0F, -0.74F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7917F, AnimationHelper.translate(0.0F, 0.0F, 1F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0833F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("leftArm", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0625F, AnimationHelper.rotation(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, AnimationHelper.rotation(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1458F, AnimationHelper.rotation(32.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1667F, AnimationHelper.rotation(33.2F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1875F, AnimationHelper.rotation(37.2F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(42.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.3646F, AnimationHelper.rotation(28.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5521F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5938F, AnimationHelper.rotation(-4.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.6458F, AnimationHelper.rotation(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7083F, AnimationHelper.rotation(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7188F, AnimationHelper.rotation(-46.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.75F, AnimationHelper.rotation(-48.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7813F, AnimationHelper.rotation(-53.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7917F, AnimationHelper.rotation(-55.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.8958F, AnimationHelper.rotation(-38.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("rightArm", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0625F, AnimationHelper.rotation(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, AnimationHelper.rotation(-32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1458F, AnimationHelper.rotation(-32.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1667F, AnimationHelper.rotation(-33.2F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.1875F, AnimationHelper.rotation(-37.2F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(-42.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.3646F, AnimationHelper.rotation(-28.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5521F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.5938F, AnimationHelper.rotation(4.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.6458F, AnimationHelper.rotation(20.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7083F, AnimationHelper.rotation(47.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7188F, AnimationHelper.rotation(46.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.75F, AnimationHelper.rotation(48.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7813F, AnimationHelper.rotation(53.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7917F, AnimationHelper.rotation(55.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.8958F, AnimationHelper.rotation(38.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(1.0833F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .looping().build();

    public static final Animation HEAD_SPIN = Animation.Builder.create(0.88542F)
            .addBoneAnimation("head", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, AnimationHelper.rotation(0.0F, 88.3F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(0.0F, 90.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, AnimationHelper.rotation(0.0F, 175.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.4167F, AnimationHelper.rotation(0.0F, 180.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, AnimationHelper.rotation(0.0F, 277.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.rotation(0.0F, 270.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, AnimationHelper.rotation(0.0F, 359.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.8333F, AnimationHelper.rotation(0.0F, 360.0F, 0.0F), Transformation.Interpolations.CATMULLROM)
                    ))
            .addBoneAnimation("left_arm", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0833F, AnimationHelper.rotation(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, AnimationHelper.rotation(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2917F, AnimationHelper.rotation(38.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, AnimationHelper.rotation(37.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.4167F, AnimationHelper.rotation(28.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, AnimationHelper.rotation(-39.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, AnimationHelper.rotation(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.rotation(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7292F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.8333F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("right_arm", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0833F, AnimationHelper.rotation(35.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, AnimationHelper.rotation(35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2917F, AnimationHelper.rotation(-38.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, AnimationHelper.rotation(-37.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.4167F, AnimationHelper.rotation(-28.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, AnimationHelper.rotation(39.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, AnimationHelper.rotation(35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.rotation(35.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.7292F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.8333F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("antenna", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.rotation(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.2083F, AnimationHelper.rotation(0.0F, -45.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.rotation(0.0F, 360.0F, 0.0F), Transformation.Interpolations.LINEAL)
                    ))
            .addBoneAnimation("antenna", new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.translate(0.0F, 0, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.0313F, AnimationHelper.translate(0.0F, -0.5F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.0625F, AnimationHelper.translate(0.0F, -2.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.0938F, AnimationHelper.translate(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, AnimationHelper.translate(0.0F, -2.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.1458F, AnimationHelper.translate(0.0F, -1.5F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, AnimationHelper.translate(0.0F, -2.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.1875F, AnimationHelper.translate(0.0F, -1.5F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, AnimationHelper.translate(0.0F, -2.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.625F, AnimationHelper.translate(0.0F, -2.0F, 0.0F), Transformation.Interpolations.LINEAL),
                    new Keyframe(0.6667F, AnimationHelper.translate(0.0F, -1.4F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.6979F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7396F, AnimationHelper.translate(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7708F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, AnimationHelper.translate(0.0F, -0.5F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.8125F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.8229F, AnimationHelper.translate(0.0F, -0.2F, 0.0F), Transformation.Interpolations.CATMULLROM),
                    new Keyframe(0.8333F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULLROM)
                    ))
            .build();
}