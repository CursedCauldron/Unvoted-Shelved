package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class SoundRegistry {

    public static final SoundEvent GLARE_GRUMPY_IDLE = new SoundEvent(UnvotedAndShelved.ID("glare_grumpy_idle"));
    public static final SoundEvent GLARE_IDLE = new SoundEvent(UnvotedAndShelved.ID("glare_idle"));
    public static final SoundEvent GLOWBERRY_DUST_STEP = new SoundEvent(UnvotedAndShelved.ID("glowberry_dust_step"));



    public static void init()
    {
        RegistryHelper.register(Registry.SOUND_EVENT, SoundEvent.class, SoundRegistry.class);
    }
}
