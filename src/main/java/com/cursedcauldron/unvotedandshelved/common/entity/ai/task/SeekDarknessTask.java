package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING;

//<>

public class SeekDarknessTask extends Behavior<GlareEntity> {
    private final int range;
    private final float speed;
    private BlockPos darkPos;


    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(DATA_GLARE_DARK_TICKS_REMAINING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.AVOID_TARGET, MemoryStatus.VALUE_ABSENT, UnvotedAndShelved.GIVEN_GLOWBERRY, MemoryStatus.VALUE_PRESENT));
        this.range = range;
        this.speed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, GlareEntity owner) {
        return (this.getDarkPos(worldIn, owner) != null) && owner.isAlive();
    }


    @Override
    protected boolean canStillUse(ServerLevel level, GlareEntity glare, long l) {
        return !glare.isInWaterOrBubble() && glare.getBrain().hasMemoryValue(DATA_GLARE_DARK_TICKS_REMAINING);
    }


    protected Vec3 getDarkPos(ServerLevel level, GlareEntity glare) {
        Random random = glare.getRandom();
        BlockPos blockPos = glare.blockPosition();

        for(int i = 0; i < 10; ++i) {
            BlockPos blockPos2 = blockPos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (((level.getBrightness(LightLayer.BLOCK, glare.blockPosition()) == 0 && level.getBrightness(LightLayer.SKY, glare.blockPosition()) == 0) || ((level.getBrightness(LightLayer.BLOCK, glare.blockPosition()) == 0 && level.getDayTime() >= 13000) || (level.getBrightness(LightLayer.BLOCK, glare.blockPosition()) == 0 && level.isThundering())))) {
                return Vec3.atBottomCenterOf(blockPos2);
            }
        }

        return null;
    }


    protected void stop(ServerLevel world, GlareEntity entity, long time) {
        System.out.println("Stopping!");
        entity.setDarkTicks(0);
    }


    protected void start(ServerLevel level, GlareEntity glare, long l) {
        System.out.println("Starting!");
        if (this.getDarkPos(level, glare) != null) {
            System.out.println("I found darkness!");
            glare.getNavigation().moveTo(this.darkPos.getX(), this.darkPos.getY() + 3, this.darkPos.getZ(), this.speed);
        }
    }
}