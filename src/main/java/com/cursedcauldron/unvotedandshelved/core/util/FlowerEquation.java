package com.cursedcauldron.unvotedandshelved.core.util;

import com.cursedcauldron.unvotedandshelved.common.entity.MoobloomEntity;
import com.cursedcauldron.unvotedandshelved.data.MoobloomTypeManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import org.jetbrains.annotations.Nullable;

/**
 * A record class that handles the status effects that each moobloom gives
 * This needs to be kept here, since the moobloom breeding system will be handled here which will include how the flowers will combine
 * @author 0rc1nus
 */
public record FlowerEquation(MoobloomEntity moobloomEntity) {

    @Nullable
    public MobEffect getEffectByItem() {
        MobEffect mobEffect = null;
        for (MoobloomType moobloomType : MoobloomTypeManager.getMoobloomTypes()) {
            if (this.moobloomEntity.getMoobloomType() == moobloomType) {
                Item item = moobloomType.getItem();
                Block block = Block.byItem(item);
                if (block instanceof FlowerBlock flowerBlock) {
                    mobEffect = flowerBlock.getSuspiciousStewEffect();
                }
            }
        }
        return mobEffect;
    }

    public int getEffectDurationByItem() {
        int duration = 0;
        for (MoobloomType moobloomType : MoobloomTypeManager.getMoobloomTypes()) {
            if (this.moobloomEntity.getMoobloomType() == moobloomType) {
                Item item = moobloomType.getItem();
                Block block = Block.byItem(item);
                if (block instanceof FlowerBlock flowerBlock) {
                    duration = flowerBlock.getEffectDuration();
                }
            }
        }
        return duration;
    }

}
