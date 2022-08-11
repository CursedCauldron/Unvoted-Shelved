package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.util.MoobloomType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.LinkedList;

public class USMoobloomTypes {
    private static final LinkedList<MoobloomType> MOOBLOOM_TYPES = new LinkedList<>();

    public static final MoobloomType ALLIUM = registerMoobloomType("allium", Items.ALLIUM);
    public static final MoobloomType AZURE_BLUET = registerMoobloomType("azure_bluet", Items.AZURE_BLUET);
    public static final MoobloomType BLOOMING_DANDELION = registerMoobloomType("blooming_dandelion", Items.ALLIUM);
    public static final MoobloomType BLUE_ORCHID = registerMoobloomType("blue_orchid", Items.BLUE_ORCHID);
    public static final MoobloomType CORNFLOWER = registerMoobloomType("cornflower", Items.CORNFLOWER);
    public static final MoobloomType DANDELION = registerMoobloomType("dandelion", Items.DANDELION);
    public static final MoobloomType LILY_OF_THE_VALLEY = registerMoobloomType("lily_of_the_valley", Items.LILY_OF_THE_VALLEY);
    public static final MoobloomType ORANGE_TULIP = registerMoobloomType("orange_tulip", Items.ORANGE_TULIP);
    public static final MoobloomType OXEYE_DAISY = registerMoobloomType("oxeye_daisy", Items.OXEYE_DAISY);
    public static final MoobloomType PINK_TULIP = registerMoobloomType("pink_tulip", Items.PINK_TULIP);
    public static final MoobloomType POPPY = registerMoobloomType("poppy", Items.POPPY);
    public static final MoobloomType RED_TULIP = registerMoobloomType("red_tulip", Items.RED_TULIP);
    public static final MoobloomType WHITE_TULIP = registerMoobloomType("white_tulip", Items.WHITE_TULIP);
    public static final MoobloomType WITHER_ROSE = registerMoobloomType("wither_rose", Items.WITHER_ROSE);

    //Compat moobloom
    //Increase the id whenever a new moobloom type is added
    //public static final MoobloomType CUSTOM_FLOWER = registerCompatMoobloomType(n + 1, "custom_flower", "custom_mod", "custom_flower");

    public static MoobloomType registerCompatMoobloomType(String name, String modid, String moddedFlowerItem) {
        MoobloomType moobloomType = new MoobloomType(name, new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_" + name + ".png"), Registry.ITEM.get(new ResourceLocation(modid, moddedFlowerItem)));
        MOOBLOOM_TYPES.add(moobloomType);
        return moobloomType;
    }

    public static MoobloomType registerMoobloomType(String name, Item item) {
        MoobloomType moobloomType = new MoobloomType(name, new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_" + name + ".png"), item);
        MOOBLOOM_TYPES.add(moobloomType);
        return moobloomType;
    }

    public static LinkedList<MoobloomType> getMoobloomTypes() {
        return MOOBLOOM_TYPES;
    }
}
