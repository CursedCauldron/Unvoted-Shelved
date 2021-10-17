package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

//<>

public class AerealStrollTask extends StrollTask {
    public AerealStrollTask(float speed) {
        super(speed);
    }

    @Override
    protected boolean shouldExecute(ServerWorld worldIn, CreatureEntity owner) {
        return owner.world.getBlockState(owner.getPosition()).isAir();
    }

    protected Vector3d findWalkTarget(CreatureEntity entityIn) {
        Vector3d walkTarget = find(entityIn, this.horizontalRadius, this.verticalRadius);
        return walkTarget != null && !entityIn.world.getBlockState(new BlockPos(walkTarget)).isAir() ? null : walkTarget;
    }

    public static Vector3d find(CreatureEntity entity, int horizontalRange, int verticalRange) {
        Vector3d vector3d = RandomPositionGenerator.findRandomTarget(entity, horizontalRange, verticalRange);

        for(int i = 0; vector3d != null && !entity.world.getBlockState(new BlockPos(vector3d)).allowsMovement(entity.world, new BlockPos(vector3d), PathType.WATER) && i++ < 10; vector3d = RandomPositionGenerator.findRandomTarget(entity, horizontalRange, verticalRange)) {
        }

        return vector3d;
    }
}