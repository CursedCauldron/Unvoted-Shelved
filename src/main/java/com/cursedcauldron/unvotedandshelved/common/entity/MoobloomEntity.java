package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.core.util.FlowerEquation;
import com.cursedcauldron.unvotedandshelved.core.util.MoobloomType;
import com.cursedcauldron.unvotedandshelved.data.MoobloomTypeManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MoobloomEntity extends Cow implements Shearable {
    private static final EntityDataAccessor<String> FLOWER_TYPE = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.STRING);
    private static final UniformInt COOLDOWN_RANGE = UniformInt.of(1200, 6000);
    private final FlowerEquation flowerEquation = new FlowerEquation(this);
    private int cooldownTicks;

    public MoobloomEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLOWER_TYPE, "allium");
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.setMoobloomType(MoobloomTypeManager.getMoobloomTypes().get(this.random.nextInt(MoobloomTypeManager.getMoobloomTypes().toArray().length)));
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    public Cow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        MoobloomEntity moobloomEntity = USEntities.MOOBLOOM.create(serverLevel);
        return moobloomEntity;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getCooldownTicks() > 0) {
            this.setCooldownTicks(this.getCooldownTicks() - 1);
        }
        if (Objects.equals(this.getFlowerType(), Objects.requireNonNull(MoobloomTypeManager.getMoobloomType("wither_rose")).getId())) {
            for (int i = 0; i < 3; i++) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX() + random.nextDouble() / 5.0, this.getY(1.0D), this.getZ() + random.nextDouble() / 5.0, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString("FlowerType", this.getFlowerType());
        compoundTag.putInt("CooldownTicks", this.getCooldownTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setFlowerType(compoundTag.getString("FlowerType"));
        this.setCooldownTicks(compoundTag.getInt("CooldownTicks"));
    }

    public MoobloomType getMoobloomType() {
        MoobloomType moobloomType = null;
        for (MoobloomType type : MoobloomTypeManager.getMoobloomTypes()) {
            if (!Objects.equals(type.getId(), this.getFlowerType())) continue;
            if (Objects.equals(type.getId(), this.getFlowerType())) {
                moobloomType = type;
            }
        }
        return moobloomType;
    }

    public void setMoobloomType(MoobloomType type) {
        this.setFlowerType(type.getId());
    }

    public String getFlowerType() {
        return this.entityData.get(FLOWER_TYPE);
    }

    public void setFlowerType(String flowerType) {
        this.entityData.set(FLOWER_TYPE, flowerType);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        for (MoobloomType moobloomType : MoobloomTypeManager.getMoobloomTypes()) {
            Item item = moobloomType.getItem();
            if (itemStack.is(item) && this.getMoobloomType() != moobloomType) {
                this.setMoobloomType(moobloomType);
                this.playSound(SoundEvents.BONE_MEAL_USE, 1.0F, 1.0F);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
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
        if (itemStack.is(Items.BONE_MEAL) && !this.level.isClientSide && this.getCooldownTicks() == 0) {
            for (int i = 0; i < UniformInt.of(1, 3).sample(random); i++) {
                this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0D), this.getZ(), new ItemStack(this.getMoobloomType().getItem())));
            }
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            this.setCooldownTicks(COOLDOWN_RANGE.sample(random));
            this.playSound(SoundEvents.BONE_MEAL_USE, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        if (itemStack.is(Items.BOWL) && !this.isBaby()) {
            ItemStack suspiciousStew = new ItemStack(Items.SUSPICIOUS_STEW);
            SuspiciousStewItem.saveMobEffect(suspiciousStew, this.getFlowerEffect(), this.getEffectDuration());
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, suspiciousStew, false));
            this.playSound(SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, interactionHand);
    }

    private int getEffectDuration() {
        return this.flowerEquation.getEffectDurationByItem();
    }

    @NotNull
    private MobEffect getFlowerEffect() {
        return Objects.requireNonNull(this.flowerEquation.getEffectByItem());
    }

    @Override
    public void shear(SoundSource soundSource) {
        Item item = this.getMoobloomType().getItem();
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
