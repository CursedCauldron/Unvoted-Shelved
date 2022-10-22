package com.cursedcauldron.unvotedandshelved.mixin.brainierbees;


import com.cursedcauldron.unvotedandshelved.common.entity.MoobloomEntity;
import com.google.common.collect.Lists;
import cursedcauldron.brainierbees.ai.ModMemoryTypes;
import cursedcauldron.brainierbees.ai.tasks.FindFlowerTask;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(FindFlowerTask.class)
public class FindFlowerTaskMixin {

    @Shadow private BlockPos flowerPosPublic;

    /**
     * @author dopadream
     * @reason moobloom support >:P
     */
    @Overwrite
    public BlockPos getFlowerPos(Bee entity, ServerLevel level) {
        int radius = 5;
        List<BlockPos> possibles = Lists.newArrayList();
        List<BlockPos> moobloomPossibles = Lists.newArrayList();

        List<MoobloomEntity> list = entity.level.getEntitiesOfClass(MoobloomEntity.class, new AABB(entity.blockPosition()).inflate(10));

        list.stream().filter(MoobloomEntity::isAlive).forEach(livingEntity -> {
            moobloomPossibles.add(livingEntity.blockPosition().above());
            this.flowerPosPublic = livingEntity.blockPosition().above();
            System.out.println(livingEntity.blockPosition());
        });

        if (moobloomPossibles.isEmpty()) {
            System.out.println("FUCK");
            for(int x = -radius; x <= radius; ++x) {
                for(int z = -radius; z <= radius; ++z) {
                    for(int y = -radius; y <= radius; ++y) {
                        BlockPos pos = new BlockPos(entity.getX() + (double)x, entity.getY() + (double)y, entity.getZ() + (double)z);
                        if (entity.level.getBlockState(pos).is(BlockTags.FLOWERS)) {
                            possibles.add(pos);
                        }
                    }
                }
            }

            if (possibles.isEmpty()) {
                entity.getBrain().setMemory(ModMemoryTypes.POLLINATING_COOLDOWN, UniformInt.of(120, 240).sample(level.getRandom()));
                return null;
            } else {
                return possibles.get(entity.getRandom().nextInt(possibles.size()));
            }
        } else {
            return moobloomPossibles.get(entity.getRandom().nextInt(moobloomPossibles.size()));
        }
    }

    /**
     * @author dopadream
     * @reason moobloom support >:P
     */
    @Overwrite
    protected void tick(ServerLevel level, Bee entity, long l) {
        if (this.flowerPosPublic != null) {
            BlockPos flowerPos = this.flowerPosPublic;
            List<MoobloomEntity> moobloomEntities = level.getEntitiesOfClass(MoobloomEntity.class, new AABB(flowerPos));
            BehaviorUtils.setWalkAndLookTargetMemories(entity, flowerPos, 0.4F, 1);
            Path flower = entity.getNavigation().createPath(flowerPos, 1);
            if (flower != null && flower.canReach()) {
                entity.getNavigation().moveTo(flower, 0.6);
                if (entity.blockPosition().closerThan(flowerPos, 2.0) && (entity.level.getBlockState(flowerPos).is(BlockTags.FLOWERS) || (moobloomEntities.size() == 1))) {
                    entity.getBrain().setMemory(ModMemoryTypes.FLOWER_POS, flowerPos);
                    this.flowerPosPublic = flowerPos;
                }
            } else {
                entity.getBrain().eraseMemory(ModMemoryTypes.FLOWER_POS);
                entity.getBrain().setMemory(ModMemoryTypes.POLLINATING_COOLDOWN, UniformInt.of(120, 240).sample(level.getRandom()));
            }
        }
    }
}
