package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

//<>

public class USEntities {

    public static void init() {
    }

    public static final EntityType<GlareEntity> GLARE = register("glare", EntityType.Builder.of(GlareEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE).sized(0.8F, 1.2F).clientTrackingRange(8));
    public static final EntityType<CopperGolemEntity> COPPER_GOLEM  = register("copper_golem", EntityType.Builder.of(CopperGolemEntity::new, MobCategory.MISC).sized(0.8F, 1.2F).clientTrackingRange(8));

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(UnvotedAndShelved.MODID, id), type.build(id));
    }
}