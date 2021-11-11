package com.cursedcauldron.unvotedandshelved.common.entity.ai.task;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING;

//<>

public class SeekDarknessTask extends Behavior<GlareEntity> {
    private final int range;
    private final float speed;
    private double wantedX;
    private double wantedY;
    private double wantedZ;

    public SeekDarknessTask(int range, float speed) {
        super(ImmutableMap.of(DATA_GLARE_DARK_TICKS_REMAINING, MemoryStatus.VALUE_PRESENT));
        System.out.println("Darkness task initiated!");
        this.range = range;
        this.speed = speed;
    }

    protected boolean checkExtraStartConditions(ServerLevel worldIn, GlareEntity owner) {
        System.out.println("Checking start conditions...");
        long time = 0;
        return setWantedPos(worldIn, owner, time);
    }




    protected boolean canStillUse(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Checking if can use..");
        return !glare.isInWaterOrBubble() && glare.getBrain().hasMemoryValue(DATA_GLARE_DARK_TICKS_REMAINING) && glare.getNavigation().isDone() && (this.setWantedPos(level, glare, time));
    }

    @Nullable
    protected Vec3 getDarkPos(ServerLevel level, GlareEntity glare) {
        Random random = glare.getRandom();
        BlockPos blockPos = glare.blockPosition();
        System.out.println("Getting dark pos...");
        for (int i = 0; i < range; ++i) {
            BlockPos blockPos2 = blockPos.offset(random.nextInt(200) - 100, random.nextInt(60) - 30, random.nextInt(200) - 100);
                if ((level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getBrightness(LightLayer.SKY, blockPos2) == 0) ||
                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.getDayTime() >= 13000) ||
                        (level.getBrightness(LightLayer.BLOCK, blockPos2) == 0 && level.isThundering()) && (level.isInWorldBounds(blockPos) && level.isEmptyBlock(blockPos))) {
                    System.out.println(blockPos2);
                    return Vec3.atBottomCenterOf(blockPos2);
                } else
                return null;
            }
        return null;
        }

    protected boolean setWantedPos(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Setting wanted pos...");
        Vec3 vec3 = this.getDarkPos(level, glare);
        if (vec3 == null) {
            glare.setDarkTicks(0);
            doStop(level, glare, time);
            return false;
        } else {
            this.wantedX = vec3.x;
            this.wantedY = vec3.y;
            this.wantedZ = vec3.z;
            return true;
        }
    }

    protected void stop(ServerLevel world, GlareEntity entity, long time) {
        System.out.println("Stopping!");
        Brain<GlareEntity> brain = entity.getBrain();
        brain.eraseMemory(UnvotedAndShelved.DATA_GLARE_DARK_TICKS_REMAINING);
        brain.eraseMemory(UnvotedAndShelved.GIVEN_GLOWBERRY);
    }

    protected void start(ServerLevel level, GlareEntity glare, long time) {
        System.out.println("Starting!");
        glare.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speed);
    }
}