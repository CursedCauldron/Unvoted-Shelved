package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.FrozenCopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.MoobloomEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;

// Entity Registry

public class USEntities {
    public static final CoreRegistry<EntityType<?>> ENTITIES = CoreRegistry.create(Registry.ENTITY_TYPE_REGISTRY, UnvotedAndShelved.MODID);

    public static final EntityType<GlareEntity> GLARE = register("glare", EntityType.Builder.of(GlareEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE).sized(0.8F, 1.2F).clientTrackingRange(8));
    public static final EntityType<CopperGolemEntity> COPPER_GOLEM = register("copper_golem", EntityType.Builder.of(CopperGolemEntity::new, MobCategory.MISC).sized(0.85F, 1.75F).clientTrackingRange(8));
    public static final EntityType<FrozenCopperGolemEntity> FROZEN_COPPER_GOLEM = register("oxidized_copper_golem",  EntityType.Builder.of(FrozenCopperGolemEntity::new, MobCategory.MISC).sized(0.85F, 1.75F).clientTrackingRange(10));
    public static final EntityType<MoobloomEntity> MOOBLOOM = register("moobloom", EntityType.Builder.of(MoobloomEntity::new, MobCategory.CREATURE).sized(0.9f, 1.4f).clientTrackingRange(10));

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return ENTITIES.register(id, type.build(id));
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GLARE, GlareEntity.createGlareAttributes());
        FabricDefaultAttributeRegistry.register(FROZEN_COPPER_GOLEM, FrozenCopperGolemEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(COPPER_GOLEM, CopperGolemEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MOOBLOOM, Cow.createAttributes());
    }
}