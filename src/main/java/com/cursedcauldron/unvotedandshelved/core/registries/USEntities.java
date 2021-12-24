package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

//<>

public class USEntities {
    public static final EntityType<GlareEntity> GLARE = register("glare", EntityType.Builder.create(GlareEntity::new, SpawnGroup.UNDERGROUND_WATER_CREATURE).setDimensions(0.8F, 1.2F).maxTrackingRange(8));
//    public static final EntityType<CopperGolemEntity> COPPER_GOLEM  = register("copper_golem", EntityType.Builder.create(CopperGolemEntity::new, SpawnGroup.MISC).setDimensions(0.8F, 1.2F).maxTrackingRange(8));

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(UnvotedAndShelved.MODID, id), type.build(id));
    }
}