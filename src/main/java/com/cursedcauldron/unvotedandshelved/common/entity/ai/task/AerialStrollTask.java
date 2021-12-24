package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

//<>

public class AerialStrollTask extends StrollTask {

	public AerialStrollTask(float speed) {
		super(speed);
	}

	protected boolean shouldRun(ServerWorld serverLevel, PathAwareEntity livingEntity) {
		return livingEntity.getNavigation().isIdle() && livingEntity.getRandom().nextInt(10) == 0;
	}

	protected boolean shouldKeepRunning(ServerWorld serverLevel, PathAwareEntity livingEntity, long l) {
		return livingEntity.getNavigation().isFollowingPath();
	}

	protected void run(ServerWorld serverLevel, PathAwareEntity pathfinderMob, long l) {
		Vec3d vec3 = this.findPos(pathfinderMob);
		if (vec3 != null) {
			LookTargetUtil.walkTowards(pathfinderMob, new BlockPos(vec3), 0.6F, 3 );
		}
	}

	@Nullable
	private Vec3d findPos(PathAwareEntity pathfinderMob) {
		Vec3d vec32;
		vec32 = pathfinderMob.getRotationVec(0.0F);
		Vec3d vec33 = AboveGroundTargeting.find(pathfinderMob, 8, 7, vec32.x, vec32.z, 1.5707964F, 3, 1);
		return vec33 != null ? vec33 : NoPenaltySolidTargeting.find(pathfinderMob, 8, 4, -2, vec32.x, vec32.z, 1.5707963705062866D);
	}
}
