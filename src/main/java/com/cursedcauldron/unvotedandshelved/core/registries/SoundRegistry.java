package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class SoundRegistry {

    public static void init() {
    }

    public static final SoundEvent GLARE_GRUMPY_IDLE = register("glare_grumpy_idle");
    public static final SoundEvent GLARE_IDLE = register("glare_idle");
    public static final SoundEvent GLOWBERRY_DUST_STEP = register("glowberry_dust_step");
    public static final SoundEvent COPPER_CLICK = register("copper_button_click");

    public static class USBlockSoundGroup {
        public static final SoundType GLOW = new VanillaSoundType(1.0f, 2.0f, () -> SoundEvents.RESPAWN_ANCHOR_CHARGE, () -> GLOWBERRY_DUST_STEP, () -> SoundEvents.RESPAWN_ANCHOR_CHARGE , () -> SoundEvents.RESPAWN_ANCHOR_CHARGE, () -> SoundEvents.RESPAWN_ANCHOR_CHARGE);
    }

    public static SoundEvent register(String key) {
        return Registry.register(Registry.SOUND_EVENT, key, new SoundEvent(new ResourceLocation(UnvotedAndShelved.MODID, key)));
    }

}
