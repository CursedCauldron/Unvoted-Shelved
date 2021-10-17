package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;

//<>

public class StrollTask extends Task<CreatureEntity> {
    private final float speed;
    protected final int horizontalRadius;
    protected final int verticalRadius;

    public StrollTask(float speed) {
        this(speed, 10, 7);
    }

    public StrollTask(float speed, int horizontalRadius, int verticalRadius) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
        this.speed = speed;
        this.horizontalRadius = horizontalRadius;
        this.verticalRadius = verticalRadius;
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, CreatureEntity entityIn, long gameTimeIn) {
        Optional<Vector3d> walkTarget = Optional.ofNullable(this.findWalkTarget(entityIn));
        entityIn.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget.map((target) -> {
            return new WalkTarget(target, this.speed, 0);
        }));
    }

    @Nullable
    protected Vector3d findWalkTarget(CreatureEntity entityIn) {
        return RandomPositionGenerator.getLandPos(entityIn, this.horizontalRadius, this.verticalRadius);
    }
}