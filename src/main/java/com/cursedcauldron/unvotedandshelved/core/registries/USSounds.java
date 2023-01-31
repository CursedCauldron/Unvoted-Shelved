package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.api.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

public class USSounds {
    public static final CoreRegistry<SoundEvent> SOUNDS = CoreRegistry.create(Registry.SOUND_EVENT_REGISTRY, UnvotedAndShelved.MODID);

    public static final SoundEvent GLARE_GRUMPY_IDLE = register("glare_grumpy_idle");
    public static final SoundEvent GLARE_IDLE = register("glare_idle");
    public static final SoundEvent GLARE_HURT = register("glare_hurt");
    public static final SoundEvent GLARE_DEATH = register("glare_death");
    public static final SoundEvent GLOWBERRY_DUST_STEP = register("glowberry_dust_step");
    public static final SoundEvent GLOWBERRY_DUST_PLACE = register("glowberry_dust_place");
    public static final SoundEvent GLOWBERRY_DUST_COLLECT = register("glowberry_dust_collect");
    public static final SoundEvent COPPER_CLICK = register("copper_button_click");
    public static final SoundEvent HEAD_SPIN = register("copper_golem_headspin");
    public static final SoundEvent HEAD_SPIN_SLOWER = register("copper_golem_headspin_slower");
    public static final SoundEvent HEAD_SPIN_SLOWEST = register("copper_golem_headspin_slowest");
    public static final SoundEvent COPPER_GOLEM_WALK = register("copper_golem_walk");
    public static final SoundEvent COPPER_GOLEM_HIT = register("copper_golem_hit");
    public static final SoundEvent COPPER_GOLEM_DEATH = register("copper_golem_death");
    public static final SoundEvent COPPER_GOLEM_REPAIR = register("copper_golem_repair");

    public static final SoundType GLOW = new SoundType(1.0F, 2.0F, USSounds.GLOWBERRY_DUST_PLACE, GLOWBERRY_DUST_STEP, USSounds.GLOWBERRY_DUST_PLACE , USSounds.GLOWBERRY_DUST_PLACE, USSounds.GLOWBERRY_DUST_PLACE);

    public static SoundEvent register(String key) {
        return SOUNDS.register(key, new SoundEvent(new ResourceLocation(UnvotedAndShelved.MODID, key)));
    }
}