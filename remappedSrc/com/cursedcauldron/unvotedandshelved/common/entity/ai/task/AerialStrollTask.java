package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.Nullable;

//<>

public class AerialStrollTask extends StrollTask {
	public static final int[][] NORMALIZED_POS_MULTIPLIERS = new int[][]{{1, 1}, {3, 3}, {5, 5}, {6, 5}, {7, 7}, {10, 7}};

	public AerialStrollTask(float speed) {
		super(speed);
	}

	protected boolean shouldRun(ServerWorld serverWorld, PathAwareEntity pathAwareEntity) {
		return pathAwareEntity.isInsideWaterOrBubbleColumn();
	}

	@Nullable
	protected Vec3d findWalkTarget(PathAwareEntity entity) {
		Vec3d entityPos = null;
		Vec3d targetPos = null;

		for (int[] range : NORMALIZED_POS_MULTIPLIERS) {
			if (entityPos == null) {
				targetPos = LookTargetUtil.find(entity, range[0], range[1]);
			} else {
				targetPos = entity.getPos().add(entity.getPos().relativize(entityPos).normalize().multiply(range[0], range[1], range[0]));
			}

			if (targetPos == null || entity.world.getBlockState(new BlockPos(targetPos)).isAir()) {
				return entityPos;
			}

			entityPos = targetPos;
		}

		return targetPos;
	}


}
