package com.cursedcauldron.unvotedandshelved.config;

import com.cursedcauldron.unvotedandshelved.config.options.UnvotedBooleanConfigOption;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class FeatureScreen extends OptionsSubScreen {
    private static final String SITE_LINK = "https://www.curseforge.com/minecraft/mc-mods/unvoted-shelved";

    public static final UnvotedBooleanConfigOption COPPER_GOLEM = new UnvotedBooleanConfigOption("copperGolem", true);
    public static final UnvotedBooleanConfigOption GLARE = new UnvotedBooleanConfigOption("glare", true);
    public static final UnvotedBooleanConfigOption RUINED_CAPITALS = new UnvotedBooleanConfigOption("ruinedCapitals", true);
    public static final UnvotedBooleanConfigOption COPPER_BUTTONS = new UnvotedBooleanConfigOption("copperButtons", true);
    public static final UnvotedBooleanConfigOption COPPER_PILLARS = new UnvotedBooleanConfigOption("copperPillars", true);

    private static final Option[] OPTIONS = new Option[]{
            GLARE.asOption().setTooltip(minecraft1 -> {
                List<FormattedCharSequence> list = minecraft1.font.split(new TranslatableComponent("options.unvotedandshelved.glare.tooltip"), 200);
                return boolean_ -> list;
            }), COPPER_GOLEM.asOption().setTooltip(minecraft1 -> {
                List<FormattedCharSequence> list = minecraft1.font.split(new TranslatableComponent("options.unvotedandshelved.copperGolem.tooltip"), 200);
                return boolean_ -> list;
            }),
            RUINED_CAPITALS.asOption().setTooltip(minecraft1 -> {
                List<FormattedCharSequence> list = minecraft1.font.split(new TranslatableComponent("options.unvotedandshelved.ruinedCapitals.tooltip"), 200);
                return boolean_ -> list;
            }),
            COPPER_BUTTONS.asOption().setTooltip(minecraft1 -> {
                List<FormattedCharSequence> list = minecraft1.font.split(new TranslatableComponent("options.unvotedandshelved.copperButtons.tooltip"), 200);
                return boolean_ -> list;
            }),
            COPPER_PILLARS.asOption().setTooltip(minecraft1 -> {
                List<FormattedCharSequence> list = minecraft1.font.split(new TranslatableComponent("options.unvotedandshelved.copperPillars.tooltip"), 200);
                return boolean_ -> list;
            })
    };
    private Screen previous;
    private OptionsList list;

    public FeatureScreen(Screen previous) {
            super(previous, Minecraft.getInstance().options, new TranslatableComponent("options.unvotedandshelved.title"));
            this.previous = previous;
        }

        protected void init() {
            this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
            this.list.addSmall(OPTIONS);
            this.addWidget(this.list);
            this.createFooter();
        }


    protected void createFooter() {
        this.addRenderableWidget(new Button(this.width / 2 - 155, this.height - 27, 150, 20, new TranslatableComponent("options.unvotedandshelved.link"), button -> this.minecraft.setScreen(new ConfirmLinkScreen(bl -> {
            if (bl) {
                Util.getPlatform().openUri(SITE_LINK);
            }
            this.minecraft.setScreen(this);
        }, SITE_LINK, true))));
        this.addRenderableWidget(new Button(this.width / 2 + 5, this.height - 27, 150, 20, CommonComponents.GUI_DONE, button -> {
            UnvotedConfigManager.save();
            this.minecraft.setScreen(this.previous);
        }));
    }

        public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
            this.renderBackground(matrices);
            this.list.render(matrices, mouseX, mouseY, delta);
            drawCenteredString(matrices, this.font, this.title, this.width / 2, 5, 16777215);
            super.render(matrices, mouseX, mouseY, delta);
            List<FormattedCharSequence> list = tooltipAt(this.list, mouseX, mouseY);
            if (list != null) {
                this.renderTooltip(matrices, list, mouseX, mouseY);
            }

        }

    public static String translationKeyOf(String type, String id) {
        return type + "." + UnvotedAndShelved.MODID + "." + id;
    }
        public void removed() {
            UnvotedConfigManager.save();
        }
}
