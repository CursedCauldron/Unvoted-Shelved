package com.cursedcauldron.unvotedandshelved.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

// Mod Config

// Written by Sydney, you're welcome! :)

@SuppressWarnings("all")

@Config(name = "unvotedandshelved")
public class ModConfig implements ConfigData {

    // Mobs

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Mobs mobs = new Mobs();

    public static class Mobs {

        @ConfigEntry.Gui.Tooltip
        public boolean glare = true;

        @ConfigEntry.Gui.Tooltip
        public boolean copper_golem = true;
    }

    // Structures

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Structures structures = new Structures();

    public static class Structures {

        @ConfigEntry.Gui.Tooltip
        public boolean ruined_capital = true;
    }

    // Blocks

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Blocks blocks = new Blocks();

    public static class Blocks {

        @ConfigEntry.Gui.Tooltip
        public boolean copper_pillars = true;

        @ConfigEntry.Gui.Tooltip
        public boolean copper_buttons = true;

        @ConfigEntry.Gui.Tooltip
        public boolean lightning_rod_variants = true;
    }
}