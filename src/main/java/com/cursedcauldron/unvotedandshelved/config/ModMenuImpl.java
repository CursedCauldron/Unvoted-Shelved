package com.cursedcauldron.unvotedandshelved.config;


import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuImpl implements ModMenuApi {
    public ModMenuImpl() {
    }

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return FeatureScreen::new;
    }
}
