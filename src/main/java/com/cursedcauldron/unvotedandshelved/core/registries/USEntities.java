package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//<>

public class USEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, UnvotedAndShelved.MODID);

    public static final RegistryObject<EntityType<GlareEntity>> GLARE   = registerEntity("glare", EntityType.Builder.create(GlareEntity::new, EntityClassification.CREATURE).size(0.9F, 1.3F).trackingRange(10));

    public static <E extends Entity> RegistryObject<EntityType<E>> registerEntity(String key, EntityType.Builder<E> entityBuilder) {
        return ENTITIES.register(key, () -> entityBuilder.build(new ResourceLocation(UnvotedAndShelved.MODID, key).toString()));
    }
}