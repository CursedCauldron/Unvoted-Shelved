package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.mixin.access.SimpleParticleTypeAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

//<>

public class USParticles {
    public static final CoreRegistry<ParticleType<?>> PARTICLES = CoreRegistry.create(Registry.PARTICLE_TYPE_REGISTRY, UnvotedAndShelved.MODID);

    public static final SimpleParticleType GLOWBERRY_DUST_PARTICLES = register("glowberry_dust", false);

    public static SimpleParticleType register(String id, boolean alwaysShow) {
        return PARTICLES.register(id, SimpleParticleTypeAccessor.createSimpleParticleType(alwaysShow));
    }
}