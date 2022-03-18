package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.entities.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.entities.GlareEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, UnvotedAndShelved.MODID);

    public static final RegistryObject<EntityType<GlareEntity>> GLARE = ENTITY_TYPES.register("glare", () -> EntityType.Builder.of(GlareEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE).sized(0.8F, 1.2F).clientTrackingRange(8).build("glare"));
    public static final RegistryObject<EntityType<CopperGolemEntity>> COPPER_GOLEM = ENTITY_TYPES.register("copper_golem", () -> EntityType.Builder.of(CopperGolemEntity::new, MobCategory.CREATURE).sized(0.8F, 1.2F).clientTrackingRange(8).build("copper_golem"));

}
