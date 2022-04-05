package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

//<>

public class AerialStrollTask extends RandomStroll {

	public AerialStrollTask(float speed) {
		super(speed);
	}

	protected boolean checkExtraStartConditions(ServerLevel serverLevel, PathfinderMob livingEntity) {
		return livingEntity.getNavigation().isDone() && livingEntity.getRandom().nextInt(10) == 0;
	}

	protected boolean shouldKeepRunning(ServerLevel serverLevel, PathfinderMob livingEntity, long l) {
		return livingEntity.getNavigation().isInProgress();
	}

	protected void start(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		Vec3 vec3 = this.findPos(pathfinderMob);
		if (vec3 != null) {
			BehaviorUtils.setWalkAndLookTargetMemories(pathfinderMob, new BlockPos(vec3), 0.6F, 3 );
		}
	}

	@Nullable
	private Vec3 findPos(PathfinderMob pathfinderMob) {
		Vec3 vec32;
		vec32 = pathfinderMob.getViewVector(0.0F);
		Vec3 vec33 = HoverRandomPos.getPos(pathfinderMob, 8, 7, vec32.x, vec32.z, 1.5707964F, 3, 1);
		return vec33 != null ? vec33 : AirAndWaterRandomPos.getPos(pathfinderMob, 8, 4, -2, vec32.x, vec32.z, 1.5707963705062866D);
	}
}
