package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.core.registries.USMoobloomTypes;
import com.cursedcauldron.unvotedandshelved.core.util.MoobloomType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class MoobloomEntity extends Cow implements Shearable {
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
        ItemStack itemStack = player.getItemInHand(interactionHand);
        for (int i : MoobloomType.getMAP().keySet()) {
            Pair<ResourceLocation, Item> pair = MoobloomType.getMAP().get(i);
            Item second = pair.getSecond();
            if (!itemStack.is(second)) continue;
            if (itemStack.is(second) && this.getFlowerType() != i) {
                this.setFlowerType(i);
                this.playSound(SoundEvents.BONE_MEAL_USE, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        if (itemStack.is(Items.SHEARS)) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level.isClientSide) {
                itemStack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(interactionHand));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, interactionHand);
    }

    @Override
    public void shear(SoundSource soundSource) {
        Item item = USMoobloomTypes.getMoobloomTypes().get(this.getFlowerType()).getItem();
        this.level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, soundSource, 1.0f, 1.0f);
        if (!this.level.isClientSide()) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.discard();
            Cow cow = EntityType.COW.create(this.level);
            cow.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            cow.setHealth(this.getHealth());
            cow.yBodyRot = this.yBodyRot;
            if (this.hasCustomName()) {
                cow.setCustomName(this.getCustomName());
                cow.setCustomNameVisible(this.isCustomNameVisible());
            }
            if (this.isPersistenceRequired()) {
                cow.setPersistenceRequired();
            }
            cow.setInvulnerable(this.isInvulnerable());
            this.level.addFreshEntity(cow);
            for (int i = 0; i < 5; ++i) {
                this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), new ItemStack(item)));
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isBaby();
    }
}
