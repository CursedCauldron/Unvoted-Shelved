package com.cursedcauldron.unvotedandshelved.entities.ai.glare.task;

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

	@Override
	protected boolean checkExtraStartConditions(ServerLevel world, PathfinderMob livingEntity) {
		return livingEntity.getNavigation().isDone() && livingEntity.getRandom().nextInt(10) == 0;
	}

	@Override
	protected boolean canStillUse(ServerLevel world, PathfinderMob mob, long p_22547_) {
		return mob.getNavigation().isInProgress();
	}

	@Override
	protected void start(ServerLevel world, PathfinderMob pathfinderMob, long p_23756_) {
		Vec3 vec3 = this.findPos(pathfinderMob);
		if (vec3 != null) {
			BehaviorUtils.setWalkAndLookTargetMemories(pathfinderMob, new BlockPos(vec3), 0.6F, 3);
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
