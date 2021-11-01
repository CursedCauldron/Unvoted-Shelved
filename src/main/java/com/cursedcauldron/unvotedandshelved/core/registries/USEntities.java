package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

//<>

public class USEntities {
    public static final EntityType<GlareEntity> GLARE = register("glare", EntityType.Builder.of(GlareEntity::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(8));

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(UnvotedAndShelved.MODID, id), type.build(id));
    }
}