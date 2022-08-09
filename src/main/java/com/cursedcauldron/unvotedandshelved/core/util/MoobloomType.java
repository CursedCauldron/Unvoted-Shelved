package com.cursedcauldron.unvotedandshelved.core.util;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Map;

public class MoobloomType {
    private final int id;
    private final Pair<ResourceLocation, Item> pair;
    private static final Map<Integer, Pair<ResourceLocation, Item>> MAP = Maps.newHashMap();

    public MoobloomType(int id, ResourceLocation resourceLocation, Item item) {
        this(id, Pair.of(resourceLocation, item));
    }

    public MoobloomType(int id, Pair<ResourceLocation, Item> pair) {
        this.pair = pair;
        this.id = id;
        MAP.put(id, pair);
    }

    public static Map<Integer, Pair<ResourceLocation, Item>> getMAP() {
        return MAP;
    }

    public Pair<ResourceLocation, Item> getPair() {
        return this.pair;
    }

    public ResourceLocation getTexture() {
        return this.pair.getFirst();
    }

    public Item getItem() {
        return this.pair.getSecond();
    }

    public int getId() {
        return this.id;
    }
}
