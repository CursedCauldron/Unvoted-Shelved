package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, UnvotedAndShelved.MODID);

    public static final RegistryObject<SimpleParticleType> GLOWBERRY_DUST_PARTICLES = register("glowberry_dust", false);

    public static RegistryObject<SimpleParticleType> register(String key, boolean alwaysShow) {
        return PARTICLE_TYPES.register(key, () -> new SimpleParticleType(alwaysShow));
    }

}
