package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

//<>

public class AerialStrollTask extends RandomStroll {
	public static final int[][] NORMALIZED_POS_MULTIPLIERS = new int[][]{{1, 1}, {3, 3}, {5, 5}, {6, 5}, {7, 7}, {10, 7}};
	private static final float HOVER_POS_OFFSET = 0.4F;

	public AerialStrollTask(float speed) {
		super(speed);
	}

	private Vec3 hoverPos;

	protected boolean checkExtraStartConditions(ServerLevel serverWorld, PathfinderMob pathAwareEntity) {
		return hoverPos != null;
	}

	@Nullable
	protected Vec3 getTargetPos(PathfinderMob entity) {
		Vec3 entityPos = null;
		Vec3 targetPos = null;

		for (int[] range : NORMALIZED_POS_MULTIPLIERS) {
			if (entityPos == null) {
				targetPos = BehaviorUtils.getRandomSwimmablePos(entity, range[0], range[1]);
			} else {
				targetPos = entity.position().add(entity.position().vectorTo(entityPos).normalize().multiply(this.hoverPos.x, this.hoverPos.y, this.hoverPos.z));
			}

			if (targetPos == null || entity.level.getBlockState(new BlockPos(targetPos)).isAir()) {
				return entityPos;
			}

			entityPos = targetPos;
		}

		return targetPos;
	}

	private float getOffset(PathfinderMob livingEntity) {
		return (livingEntity.getRandom().nextFloat() * 2.0F - 1.0F) * 0.33333334F;
	}

	protected void tick(ServerLevel serverLevel, PathfinderMob livingEntity, long l) {
		boolean bl = livingEntity.position().distanceTo(this.hoverPos) <= 0.1D;
		boolean bl2 = true;
		Vec3 vec3 = Vec3.atBottomCenterOf(livingEntity.blockPosition()).add(0.0D, 0.6000000238418579D, 0.0D);

		if (bl) {
			boolean bl3 = livingEntity.getRandom().nextInt(25) == 0;
			if (bl3) {
				this.hoverPos = new Vec3(vec3.x() + (double) this.getOffset(livingEntity), vec3.y(), vec3.z() + (double) this.getOffset(livingEntity));
				livingEntity.getNavigation().stop();
			} else {
				bl2 = false;
			}

			livingEntity.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
		}

		if (bl2) {
			this.setWantedPos(livingEntity);
		}
	}

	private void setWantedPos(PathfinderMob livingEntity) {
		livingEntity.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355D);
	}
}