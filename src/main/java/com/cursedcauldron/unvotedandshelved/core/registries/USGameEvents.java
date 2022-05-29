package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;

public class USGameEvents {

    public static final GameEvent SPIN_HEAD = new GameEvent("spin_head", 16);

    public static void init() {
        Registry.register(Registry.GAME_EVENT, new ResourceLocation(UnvotedAndShelved.MODID, "spin_head"), SPIN_HEAD);
    }

}
