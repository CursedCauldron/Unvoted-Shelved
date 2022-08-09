package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.core.util.MoobloomType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MoobloomEntity extends Cow {
    private static final EntityDataAccessor<Integer> FLOWER_TYPE = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.INT);

    public MoobloomEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLOWER_TYPE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("FlowerType", this.getFlowerType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setFlowerType(compoundTag.getInt("FlowerType"));
    }

    public int getFlowerType() {
        return this.entityData.get(FLOWER_TYPE);
    }

    public void setFlowerType(int flowerType) {
        this.entityData.set(FLOWER_TYPE, flowerType);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        for (int i : MoobloomType.getMAP().keySet()) {
            Pair<ResourceLocation, Item> pair = MoobloomType.getMAP().get(i);
            Item second = pair.getSecond();
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (!itemInHand.is(second)) continue;
            if (itemInHand.is(second) && this.getFlowerType() != i) {
                this.setFlowerType(i);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, interactionHand);
    }
}
