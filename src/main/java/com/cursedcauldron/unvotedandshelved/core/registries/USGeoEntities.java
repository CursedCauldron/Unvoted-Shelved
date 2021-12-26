package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import software.bernie.example.registry.EntityRegistryBuilder;

public class USGeoEntities {

    public static final EntityType<CopperGolemEntity> COPPER_GOLEM = buildEntity(CopperGolemEntity::new,
            CopperGolemEntity.class, .8F, 1.2F, SpawnGroup.MISC);

    public static <T extends Entity> EntityType<T> buildEntity(EntityType.EntityFactory<T> entity, Class<T> entityClass,
                                                               float width, float height, SpawnGroup group) {
        String name = "copper_golem";
        return EntityRegistryBuilder.<T>createBuilder(new Identifier(UnvotedAndShelved.MODID, name)).entity(entity)
                .category(group).dimensions(EntityDimensions.changing(width, height)).build();
    }
}
