package com.cursedcauldron.unvotedandshelved.config.options;

import com.cursedcauldron.unvotedandshelved.config.FeatureScreen;
import com.terraformersmc.modmenu.config.option.ConfigOptionStorage;
import com.terraformersmc.modmenu.config.option.OptionConvertable;
import net.minecraft.client.CycleOption;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class UnvotedBooleanConfigOption implements OptionConvertable {
    private final String key;
    private final String translationKey;
    private final boolean defaultValue;
    private final TranslatableComponent enabledText;
    private final TranslatableComponent disabledText;

    public UnvotedBooleanConfigOption(String key, boolean defaultValue, String enabledKey, String disabledKey) {
        ConfigOptionStorage.setBoolean(key, defaultValue);
        this.key = key;
        this.translationKey = FeatureScreen.translationKeyOf("options", key);
        this.defaultValue = defaultValue;
        this.enabledText = new TranslatableComponent(this.translationKey + "." + enabledKey);
        this.disabledText = new TranslatableComponent(this.translationKey + "." + disabledKey);
    }

    public UnvotedBooleanConfigOption(String key, boolean defaultValue) {
        this(key, defaultValue, "true", "false");
    }

    public String getKey() {
        return this.key;
    }

    public boolean getValue() {
        return ConfigOptionStorage.getBoolean(this.key);
    }

    public void setValue(boolean value) {
        ConfigOptionStorage.setBoolean(this.key, value);
    }

    public void toggleValue() {
        ConfigOptionStorage.toggleBoolean(this.key);
    }

    public boolean getDefaultValue() {
        return this.defaultValue;
    }

    public Component getButtonText() {
        return CommonComponents.optionNameValue(new TranslatableComponent(this.translationKey), this.getValue() ? this.enabledText : this.disabledText);
    }


    public CycleOption<Boolean> asOption() {
        return this.enabledText != null && this.disabledText != null ? CycleOption.createBinaryOption(this.translationKey, this.enabledText, this.disabledText, (ignored) -> {
            return ConfigOptionStorage.getBoolean(this.key);
        }, (ignored, option, value) -> {
            ConfigOptionStorage.setBoolean(this.key, value);
        }) : CycleOption.createOnOff(this.translationKey, (ignored) -> {
            return ConfigOptionStorage.getBoolean(this.key);
        }, (ignored, option, value) -> {
            ConfigOptionStorage.setBoolean(this.key, value);
        });
    }
}
