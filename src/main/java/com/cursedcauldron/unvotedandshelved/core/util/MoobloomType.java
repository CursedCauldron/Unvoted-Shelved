package com.cursedcauldron.unvotedandshelved.core.util;

import com.cursedcauldron.unvotedandshelved.data.MoobloomTypeManager;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.Objects;

/**
 * A utility class for storing the data of a moobloom type
 * @author 0rc1nus
 */
public class MoobloomType {
    private final String id;
    private final Pair<ResourceLocation, Item> pair;

    public MoobloomType(String id, ResourceLocation resourceLocation, Item item) {
        this(id, Pair.of(resourceLocation, item));
    }

    public MoobloomType(String id, Pair<ResourceLocation, Item> pair) {
        this.pair = pair;
        this.id = id;
    }

    public ResourceLocation getTexture() {
        return this.pair.getFirst();
    }

    public Item getItem() {
        return this.pair.getSecond();
    }

    public String getId() {
        return this.id;
    }

    public static MoobloomType matchesTypeById(String id) {
        for (MoobloomType type : MoobloomTypeManager.getMoobloomTypes()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }

}
