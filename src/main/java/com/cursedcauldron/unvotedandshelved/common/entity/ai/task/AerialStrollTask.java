package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

//<>

public class AerialStrollTask extends RandomStroll {
	public static final int[][] NORMALIZED_POS_MULTIPLIERS = new int[][]{{1, 1}, {3, 3}, {5, 5}, {6, 5}, {7, 7}, {10, 7}};

	public AerialStrollTask(float speed) {
		super(speed);
	}

	protected boolean checkExtraStartConditions(ServerLevel serverWorld, PathfinderMob pathAwareEntity) {
		return pathAwareEntity.isInWaterOrBubble();
	}

	@Nullable
	protected Vec3 getTargetPos(PathfinderMob entity) {
		Vec3 entityPos = null;
		Vec3 targetPos = null;

		for (int[] range : NORMALIZED_POS_MULTIPLIERS) {
			if (entityPos == null) {
				targetPos = BehaviorUtils.getRandomSwimmablePos(entity, range[0], range[1]);
			} else {
				targetPos = entity.position().add(entity.position().vectorTo(entityPos).normalize().multiply(range[0], range[1], range[0]));
			}

			if (targetPos == null || entity.level.getBlockState(new BlockPos(targetPos)).isAir()) {
				return entityPos;
			}

			entityPos = targetPos;
		}

		return targetPos;
	}


}
