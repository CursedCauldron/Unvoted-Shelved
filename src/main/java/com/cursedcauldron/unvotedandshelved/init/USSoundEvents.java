package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UnvotedAndShelved.MODID);

    public static final RegistryObject<SoundEvent> GLARE_GRUMPY_IDLE = register("glare_grumpy_idle");
    public static final RegistryObject<SoundEvent> GLARE_IDLE = register("glare_idle");
    public static final RegistryObject<SoundEvent> GLOWBERRY_DUST_STEP = register("glowberry_dust_step");
    public static final RegistryObject<SoundEvent> COPPER_CLICK = register("copper_button_click");
    public static final RegistryObject<SoundEvent> HEAD_SPIN = register("copper_golem_headspin");
    public static final RegistryObject<SoundEvent> HEAD_SPIN_SLOWER = register("copper_golem_headspin_slower");
    public static final RegistryObject<SoundEvent> HEAD_SPIN_SLOWEST = register("copper_golem_headspin_slowest");
    public static final RegistryObject<SoundEvent> CHINESE_RIP_OFF_WINNIE_THE_POOH = register("chinese_rip_off_winnie_the_pooh");

    public static class USBlockSoundGroup {
        public static final SoundType GLOW = new ForgeSoundType(1.0f, 2.0f, () -> SoundEvents.RESPAWN_ANCHOR_CHARGE, GLOWBERRY_DUST_STEP, () -> SoundEvents.RESPAWN_ANCHOR_CHARGE , () -> SoundEvents.RESPAWN_ANCHOR_CHARGE, () -> SoundEvents.RESPAWN_ANCHOR_CHARGE);
    }

    public static RegistryObject<SoundEvent> register(String key) {
        return SOUND_EVENTS.register(key, () -> new SoundEvent(new ResourceLocation(UnvotedAndShelved.MODID, key)));
    }


}
