package com.cursedcauldron.unvotedandshelved.common.entity;

import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.core.registries.USItems;
import com.cursedcauldron.unvotedandshelved.mixin.ThrownTridentAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FrozenCopperGolemEntity extends AbstractGolem {
    private static final EntityDimensions MARKER_DIMENSIONS = new EntityDimensions(0.0f, 0.0f, true);
    private static final EntityDimensions BABY_DIMENSIONS = EntityType.ARMOR_STAND.getDimensions().scale(0.5f);
    public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = SynchedEntityData.defineId(FrozenCopperGolemEntity.class, EntityDataSerializers.BYTE);
    private static final Predicate<Entity> RIDABLE_MINECARTS = entity -> entity instanceof AbstractMinecart && ((AbstractMinecart)entity).getMinecartType() == AbstractMinecart.Type.RIDEABLE;
    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
    private boolean invisible;
    public long lastHit;

    public FrozenCopperGolemEntity(EntityType<? extends FrozenCopperGolemEntity> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 0.0f;
    }

    @Override
    public void refreshDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPos(x, y, z);
    }

    // Sets gravity for the Oxidized Copper Golem similarly to Armor Stands

    private boolean hasPhysics() {
        return !this.isMarker() && !this.isNoGravity();
    }

    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi() && this.hasPhysics();
    }

    // NBT Data

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CLIENT_FLAGS, (byte)0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.hasCustomName();
    }

    // Prevents the Oxidized Copper Golem from being pushed

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    protected void pushEntities() {
        List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), RIDABLE_MINECARTS);
        for (Entity entity : list) {
            if (this.distanceToSqr(entity) <= 0.2) {
                entity.push(this);
            }
        }
    }

    // Converts the Oxidized Copper Golem back into a normal Copper Golem if the player scrapes it with an Axe

    @Override
    public InteractionResult interactAt(Player player, @NotNull Vec3 vec3, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof AxeItem) {
            this.convertBack(USEntities.COPPER_GOLEM, true);
            this.gameEvent(GameEvent.ENTITY_INTERACT, this);
            this.level.playSound(player, this.blockPosition(), SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.level.levelEvent(player, 3005, this.blockPosition(), 0);
            return InteractionResult.SUCCESS;
        } else return InteractionResult.PASS;
    }

    // Sets all saved attributes when the Oxidized Copper Golem is converted back into a normal Copper Golem

    public <T extends Mob> void convertBack(EntityType<T> entityType, boolean bl) {
        if (this.isRemoved()) {
            return;
        }
        CopperGolemEntity mob = (CopperGolemEntity)entityType.create(this.level);
        assert mob != null;
        mob.copyPosition(this);
        mob.setXRot(this.xRotO);
        mob.setYRot(this.yRotO);
        mob.setYBodyRot(this.yBodyRotO);
        mob.setYHeadRot(this.getYHeadRot());
        mob.setBaby(this.isBaby());
        mob.setNoAi(this.isNoAi());
        mob.setStage(CopperGolemEntity.Stage.WEATHERED);
        if (this.hasCustomName()) {
            mob.setCustomName(this.getCustomName());
            mob.setCustomNameVisible(this.isCustomNameVisible());
        }
        if (this.isPersistenceRequired()) {
            mob.setPersistenceRequired();
        }
        mob.setInvulnerable(this.isInvulnerable());
        if (bl) {
            mob.setCanPickUpLoot(this.canPickUpLoot());
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                ItemStack itemStack = this.getItemBySlot(equipmentSlot);
                if (itemStack.isEmpty()) continue;
                mob.setItemSlot(equipmentSlot, itemStack.copy());
                mob.setDropChance(equipmentSlot, this.getEquipmentDropChance(equipmentSlot));
                itemStack.setCount(0);
            }
        }
        this.level.addFreshEntity(mob);
        if (this.isPassenger()) {
            Entity entity = this.getVehicle();
            this.stopRiding();
            assert entity != null;
            mob.startRiding(entity, true);
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float f) {

        // Prevents the Copper Golem from being damaged by most damage sources

        if (this.level.isClientSide || this.isRemoved()) {
            return false;
        }
        if (DamageSource.OUT_OF_WORLD.equals(damageSource)) {
            this.kill();
            return false;
        }
        if (this.isInvulnerableTo(damageSource) || this.invisible || this.isMarker()) {
            return false;
        }
        if (damageSource.isExplosion()) {
            this.brokenByAnything(damageSource);
            this.kill();
            return false;
        }
        if (DamageSource.IN_FIRE.equals(damageSource)) {

            // Sets the Oxidized Copper Golem on fire if standing in fire

            if (this.isOnFire()) {
                this.causeDamage(damageSource, 0.15f);
            } else {
                this.setSecondsOnFire(5);
            }
            return false;
        }
        if (DamageSource.ON_FIRE.equals(damageSource) && this.getHealth() > 0.5f) {
            this.causeDamage(damageSource, 4.0f);
            return false;
        }

        // Prevents Oxidized Copper Golem from being immune to Wardens

        if (damageSource.getDirectEntity() instanceof Warden) {
            this.kill();
            Block.popResource(this.level, this.blockPosition(), new ItemStack(USItems.FROZEN_COPPER_GOLEM_ITEM));
            return false;
        }

        // Prevents Oxidized Copper Golem from being immune to Withers

        if (damageSource.getDirectEntity() instanceof WitherBoss) {
            this.kill();
            Block.popResource(this.level, this.blockPosition(), new ItemStack(USItems.FROZEN_COPPER_GOLEM_ITEM));
            return false;
        }

        // Unoxidizes the Copper Golem if it is struck by a Trident with Channelling

        boolean bl = damageSource.getDirectEntity() instanceof AbstractArrow && !(damageSource.getDirectEntity() instanceof ThrownTrident);
        boolean bl2 = bl && ((AbstractArrow)damageSource.getDirectEntity()).getPierceLevel() > 0;
        boolean bl3 = "player".equals(damageSource.getMsgId());
        if (!bl3 && !bl) {
            return false;
        }
        if (damageSource.getDirectEntity() instanceof ThrownTrident trident && EnchantmentHelper.hasChanneling(((ThrownTridentAccessor)trident).getTridentItem()) && this.level.canSeeSky(this.blockPosition())) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(this.level);
            assert lightningBolt != null;
            lightningBolt.moveTo(Vec3.atBottomCenterOf(this.blockPosition()));
            lightningBolt.setCause(damageSource.getEntity() instanceof ServerPlayer ? (ServerPlayer)damageSource.getEntity() : null);
            this.level.addFreshEntity(lightningBolt);
            this.playSound(SoundEvents.TRIDENT_THUNDER, 5.0F, 1.0f);
        }

        // Prevents the Oxidized Copper Golem from being destroyed by players who don't have building permissions

        if (damageSource.getEntity() instanceof Player && !((Player)damageSource.getEntity()).getAbilities().mayBuild) {
            return false;
        }

        // Allows players in Creative Mode to instantly destroy the Oxidized Copper Golem

        if (damageSource.isCreativePlayer()) {
            this.playBrokenSound();
            this.showBreakingParticles();
            this.kill();
            return bl2;
        }

        // Lets players who are able to break the Oxidized Copper Golem break it

        long l = this.level.getGameTime();
        if (l - this.lastHit <= 5L || bl) {
            this.brokenByPlayer(damageSource);
            this.showBreakingParticles();
            this.kill();
        } else {
            this.level.broadcastEntityEvent(this, (byte)32);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
            this.lastHit = l;
        }
        return true;
    }

    // Plays the copper hit sound when hitting an Oxidized Copper Golem

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 32) {
            if (this.level.isClientSide) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.COPPER_HIT, this.getSoundSource(), 0.3f, 1.0f, false);
                this.lastHit = this.level.getGameTime();
            }
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double d) {
        double e = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(e) || e == 0.0) {
            e = 4.0;
        }
        return d < (e *= 64.0) * e;
    }

    // Shows breaking particles when Oxidized Copper Golem is broken

    private void showBreakingParticles() {
        if (this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OXIDIZED_COPPER.defaultBlockState()), this.getX(), this.getY(0.6666666666666666), this.getZ(), 10, this.getBbWidth() / 4.0f, this.getBbHeight() / 4.0f, this.getBbWidth() / 4.0f, 0.05);
        }
    }

    // Lets the Oxidized Copper Golem take damage

    private void causeDamage(DamageSource damageSource, float f) {
        float g = this.getHealth();
        if ((g -= f) <= 0.5f) {
            this.brokenByAnything(damageSource);
            this.kill();
        } else {
            this.setHealth(g);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
        }
    }

    // Drops the Oxidized Copper Golem as an item if broken

    private void brokenByPlayer(DamageSource damageSource) {
        Block.popResource(this.level, this.blockPosition(), new ItemStack(USItems.FROZEN_COPPER_GOLEM_ITEM));
        this.brokenByAnything(damageSource);
    }

    public final void brokenByAnything(DamageSource damageSource) {
        ItemStack itemStack;
        int i;
        this.playBrokenSound();
        this.dropAllDeathLoot(damageSource);
        for (i = 0; i < this.handItems.size(); ++i) {
            itemStack = this.handItems.get(i);
            if (itemStack.isEmpty()) continue;
            Block.popResource(this.level, this.blockPosition().above(), itemStack);
            this.handItems.set(i, ItemStack.EMPTY);
        }
        for (i = 0; i < this.armorItems.size(); ++i) {
            itemStack = this.armorItems.get(i);
            if (itemStack.isEmpty()) continue;
            Block.popResource(this.level, this.blockPosition().above(), itemStack);
            this.armorItems.set(i, ItemStack.EMPTY);
        }
    }

    // Plays copper breaking sound when Oxidized Copper Golem is broken

    private void playBrokenSound() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.COPPER_BREAK, this.getSoundSource(), 1.0f, 1.0f);
    }

    @Override
    protected float tickHeadTurn(float f, float g) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.getYRot();
        return 0.0f;
    }

    // Sets the eye height of the mob

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * (this.isBaby() ? 0.3f : 0.6f);
    }

    @Override
    public double getMyRidingOffset() {
        return this.isMarker() ? 0.0 : (double)0.1f;
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (!this.hasPhysics()) {
            return;
        }
        super.travel(vec3);
    }

    // Unoxidizes the Copper Golem if struck by lightning

    @Override
    public void thunderHit(@NotNull ServerLevel serverLevel, @NotNull LightningBolt lightningBolt) {
        this.convertBack(USEntities.COPPER_GOLEM, true);
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 2.0f, 0.5f + this.random.nextFloat() * 0.2f, false);
        this.level.levelEvent(3004, this.blockPosition(), 0);
    }

    @Override
    public void setYBodyRot(float f) {
        this.yBodyRotO = this.yRotO = f;
        this.yHeadRotO = this.yHeadRot = f;
    }

    @Override
    public void setYHeadRot(float f) {
        this.yBodyRotO = this.yRotO = f;
        this.yHeadRotO = this.yHeadRot = f;
    }

    // Allows for the Oxidized Copper Golem to be set as invisible

    @Override
    protected void updateInvisibilityStatus() {
        this.setInvisible(this.invisible);
    }

    @Override
    public void setInvisible(boolean bl) {
        this.invisible = bl;
        super.setInvisible(bl);
    }

    @Override
    public boolean isBaby() {
        return this.isSmall();
    }

    @Override
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
    }

    @Override
    public boolean ignoreExplosion() {
        return this.isInvisible();
    }

    // Piston pushing reaction

    @Override
    public PushReaction getPistonPushReaction() {
        if (this.isMarker()) {
            return PushReaction.IGNORE;
        }
        return super.getPistonPushReaction();
    }

    public boolean isSmall() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 1) != 0;
    }


    public boolean isMarker() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 0x10) != 0;
    }

    // Entity Attributes

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public boolean isPickable() {
        return super.isPickable() && !this.isMarker();
    }

    @Override
    public boolean skipAttackInteraction(@NotNull Entity entity) {
        return entity instanceof Player && !this.level.mayInteract((Player)entity, this.blockPosition());
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    // Sound Events

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.COPPER_FALL, SoundEvents.COPPER_FALL);
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.COPPER_HIT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.COPPER_BREAK;
    }

    // Makes it so that fully oxidized Copper Golems are not affected by potion effects

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> entityDataAccessor) {
        if (DATA_CLIENT_FLAGS.equals(entityDataAccessor)) {
            this.refreshDimensions();
            this.blocksBuilding = !this.isMarker();
        }
        super.onSyncedDataUpdated(entityDataAccessor);
    }

    // Prevents the Oxidized Copper Golem from being attacked

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(@NotNull Pose pose) {
        return this.getDimensionsMarker(this.isMarker());
    }

    private EntityDimensions getDimensionsMarker(boolean bl) {
        if (bl) {
            return MARKER_DIMENSIONS;
        }
        return this.isBaby() ? BABY_DIMENSIONS : this.getType().getDimensions();
    }

    @Override
    public Vec3 getLightProbePosition(float f) {
        if (this.isMarker()) {
            AABB aABB = this.getDimensionsMarker(false).makeBoundingBox(this.position());
            BlockPos blockPos = this.blockPosition();
            int i = Integer.MIN_VALUE;
            for (BlockPos blockPos2 : BlockPos.betweenClosed(new BlockPos(aABB.minX, aABB.minY, aABB.minZ), new BlockPos(aABB.maxX, aABB.maxY, aABB.maxZ))) {
                int j = Math.max(this.level.getBrightness(LightLayer.BLOCK, blockPos2), this.level.getBrightness(LightLayer.SKY, blockPos2));
                if (j == 15) {
                    return Vec3.atCenterOf(blockPos2);
                }
                if (j <= i) continue;
                i = j;
                blockPos = blockPos2.immutable();
            }
            return Vec3.atCenterOf(blockPos);
        }
        return super.getLightProbePosition(f);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(USItems.FROZEN_COPPER_GOLEM_ITEM);
    }

    @Override
    public boolean canBeSeenByAnyone() {
        return !this.isInvisible() && !this.isMarker();
    }
}
