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

    public static final MoobloomType ALLIUM = registerMoobloomType(0,"allium", Items.ALLIUM);
    public static final MoobloomType AZURE_BLUET = registerMoobloomType(1, "azure_bluet", Items.AZURE_BLUET);
    public static final MoobloomType BLOOMING_DANDELION = registerMoobloomType(2, "blooming_dandelion", Items.ALLIUM);
    public static final MoobloomType BLUE_ORCHID = registerMoobloomType(3, "blue_orchid", Items.BLUE_ORCHID);
    public static final MoobloomType CORNFLOWER = registerMoobloomType(4, "cornflower", Items.CORNFLOWER);
    public static final MoobloomType DANDELION = registerMoobloomType(5, "dandelion", Items.DANDELION);
    public static final MoobloomType LILY_OF_THE_VALLEY = registerMoobloomType(6, "lily_of_the_valley", Items.LILY_OF_THE_VALLEY);
    public static final MoobloomType ORANGE_TULIP = registerMoobloomType(7, "orange_tulip", Items.ORANGE_TULIP);
    public static final MoobloomType OXEYE_DAISY = registerMoobloomType(8, "oxeye_daisy", Items.OXEYE_DAISY);
    public static final MoobloomType PINK_TULIP = registerMoobloomType(9, "pink_tulip", Items.PINK_TULIP);
    public static final MoobloomType POPPY = registerMoobloomType(10, "poppy", Items.POPPY);
    public static final MoobloomType RED_TULIP = registerMoobloomType(11, "red_tulip", Items.RED_TULIP);
    public static final MoobloomType WHITE_TULIP = registerMoobloomType(12, "white_tulip", Items.WHITE_TULIP);
    public static final MoobloomType WITHER_ROSE = registerMoobloomType(13, "wither_rose", Items.WITHER_ROSE);

    //Compat moobloom
    //Increase the id whenever a new moobloom type is added
    //public static final MoobloomType CUSTOM_FLOWER = registerCompatMoobloomType(n + 1, "custom_flower", "custom_mod", "custom_flower");

    public static MoobloomType registerCompatMoobloomType(int id, String name, String modid, String moddedFlowerItem) {
        MoobloomType moobloomType = new MoobloomType(id, new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_" + name + ".png"), Registry.ITEM.get(new ResourceLocation(modid, moddedFlowerItem)));
        MOOBLOOM_TYPES.add(moobloomType);
        return moobloomType;
    }

    public static MoobloomType registerMoobloomType(int id, String name, Item item) {
        MoobloomType moobloomType = new MoobloomType(id, new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_" + name + ".png"), item);
        MOOBLOOM_TYPES.add(moobloomType);
        return moobloomType;
    }

    public static LinkedList<MoobloomType> getMoobloomTypes() {
        return MOOBLOOM_TYPES;
    }
}
