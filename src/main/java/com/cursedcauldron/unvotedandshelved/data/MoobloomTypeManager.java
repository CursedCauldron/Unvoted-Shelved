package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.util.MoobloomType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MoobloomTypeManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder().create();
    private static final LinkedList<MoobloomType> MOOBLOOM_TYPES = Lists.newLinkedList();

    public MoobloomTypeManager() {
        super(GSON_INSTANCE, "moobloom_types");
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(UnvotedAndShelved.MODID, "moobloom_types");
    }

    public static LinkedList<MoobloomType> getMoobloomTypes() {
        return MOOBLOOM_TYPES;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManagerIn, ProfilerFiller profilerFiller) {
        ResourceLocation file = new ResourceLocation(UnvotedAndShelved.MODID, "moobloom_types/moobloom_type_table.json");
        try {
            for (Resource iResource : resourceManagerIn.getResourceStack(file)) {
                try (Reader reader = new BufferedReader(new InputStreamReader(iResource.open(), StandardCharsets.UTF_8))) {
                    JsonObject jsonObject = GsonHelper.fromJson(GSON_INSTANCE, reader, JsonObject.class);
                    if (jsonObject != null) {
                        JsonArray entryList = jsonObject.get("entries").getAsJsonArray();
                        for (JsonElement entry : entryList) {
                            String name = entry.getAsJsonObject().get("name").getAsString();
                            ResourceLocation resourceLocation = new ResourceLocation(UnvotedAndShelved.MODID, "textures/entity/moobloom/moobloom_" + name + ".png");
                            Item item = Registry.ITEM.get(new ResourceLocation(entry.getAsJsonObject().get("id").getAsString(), entry.getAsJsonObject().get("name").getAsString()));
                            MoobloomType moobloomType = new MoobloomType(name, resourceLocation, item);
                            MOOBLOOM_TYPES.add(moobloomType);
                        }
                    }
                } catch (RuntimeException | IOException exception) {
                    UnvotedAndShelved.LOGGER.error("Couldn't read moobloom type table list {} in data pack {}", file, iResource.sourcePackId(), exception);
                }
            }
        } catch (NoSuchElementException exception) {
            UnvotedAndShelved.LOGGER.error("Couldn't read moobloom type table from {}", file, exception);
        }
    }

    public static MoobloomType getMoobloomType(String id) {
        for (MoobloomType moobloomType : MOOBLOOM_TYPES) {
            if (Objects.equals(moobloomType.getId(), id)) {
                return moobloomType;
            }
        }
        return null;
    }

    public static ResourceLocation getTexture(MoobloomType moobloomType) {
        for (MoobloomType type : MOOBLOOM_TYPES) {
            if (moobloomType == type) {
                return type.getTexture();
            }
        }
        return null;
    }
}
