package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class USSounds {
    public static final CoreRegistry<SoundEvent> SOUNDS = CoreRegistry.create(Registry.SOUND_EVENT_REGISTRY, UnvotedAndShelved.MODID);

    public static final SoundEvent GLARE_GRUMPY_IDLE = register("glare_grumpy_idle");
    public static final SoundEvent GLARE_IDLE = register("glare_idle");
    public static final SoundEvent GLOWBERRY_DUST_STEP = register("glowberry_dust_step");
    public static final SoundEvent COPPER_CLICK = register("copper_button_click");
    public static final SoundEvent HEAD_SPIN = register("copper_golem_headspin");
    public static final SoundEvent HEAD_SPIN_SLOWER = register("copper_golem_headspin_slower");
    public static final SoundEvent HEAD_SPIN_SLOWEST = register("copper_golem_headspin_slowest");
    public static final SoundEvent CHINESE_RIP_OFF_WINNIE_THE_POOH = register("chinese_rip_off_winnie_the_pooh");

    public static final SoundType GLOW                  = new SoundType(1.0F, 2.0F, SoundEvents.RESPAWN_ANCHOR_CHARGE, GLOWBERRY_DUST_STEP, SoundEvents.RESPAWN_ANCHOR_CHARGE , SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundEvents.RESPAWN_ANCHOR_CHARGE);

    public static SoundEvent register(String key) {
        return SOUNDS.register(key, new SoundEvent(new ResourceLocation(UnvotedAndShelved.MODID, key)));
    }
}