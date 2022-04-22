package com.cursedcauldron.unvotedandshelved.config;

import com.cursedcauldron.unvotedandshelved.config.options.UnvotedBooleanConfigOption;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.terraformersmc.modmenu.config.option.ConfigOptionStorage;
import com.terraformersmc.modmenu.config.option.EnumConfigOption;
import com.terraformersmc.modmenu.config.option.StringSetConfigOption;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.stream.Collectors;

public class UnvotedConfigManager {
    private static File file;


    private static void prepareConfigFile() {
        if (file != null) {
            return;
        }
        file = new File(FabricLoader.getInstance().getConfigDir().toFile(), UnvotedAndShelved.MODID + ".json");
    }

    public static void initializeConfig() {
        load();
    }

    @SuppressWarnings("unchecked")
    private static void load() {
        prepareConfigFile();

        try {
            if (!file.exists()) {
                save();
            }
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                JsonObject json = new JsonParser().parse(br).getAsJsonObject();

                for (Field field : FeatureScreen.class.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                        if (StringSetConfigOption.class.isAssignableFrom(field.getType())) {
                            JsonArray jsonArray = json.getAsJsonArray(field.getName().toLowerCase(Locale.ROOT));
                            if (jsonArray != null) {
                                StringSetConfigOption option = (StringSetConfigOption) field.get(null);
                                ConfigOptionStorage.setStringSet(option.getKey(), Sets.newHashSet(jsonArray).stream().map(JsonElement::getAsString).collect(Collectors.toSet()));
                            }
                        } else if (UnvotedBooleanConfigOption.class.isAssignableFrom(field.getType())) {
                            JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive(field.getName().toLowerCase(Locale.ROOT));
                            if (jsonPrimitive != null && jsonPrimitive.isBoolean()) {
                                UnvotedBooleanConfigOption option = (UnvotedBooleanConfigOption) field.get(null);
                                ConfigOptionStorage.setBoolean(option.getKey(), jsonPrimitive.getAsBoolean());
                            }
                        } else if (EnumConfigOption.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
                            JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive(field.getName().toLowerCase(Locale.ROOT));
                            if (jsonPrimitive != null && jsonPrimitive.isString()) {
                                Type generic = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                                if (generic instanceof Class<?>) {
                                    EnumConfigOption<?> option = (EnumConfigOption<?>) field.get(null);
                                    Enum<?> found = null;
                                    for (Enum<?> value : ((Class<Enum<?>>) generic).getEnumConstants()) {
                                        if (value.name().toLowerCase(Locale.ROOT).equals(jsonPrimitive.getAsString())) {
                                            found = value;
                                            break;
                                        }
                                    }
                                    if (found != null) {
                                        ConfigOptionStorage.setEnumTypeless(option.getKey(), found);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException | IllegalAccessException e) {
            System.err.println("Couldn't load Unvoted & Shelved configuration file; reverting to defaults");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void save() {
        prepareConfigFile();

        JsonObject config = new JsonObject();

        try {
            for (Field field : FeatureScreen.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                    if (UnvotedBooleanConfigOption.class.isAssignableFrom(field.getType())) {
                        UnvotedBooleanConfigOption option = (UnvotedBooleanConfigOption) field.get(null);
                        config.addProperty(field.getName().toLowerCase(Locale.ROOT), ConfigOptionStorage.getBoolean(option.getKey()));
                    } else if (StringSetConfigOption.class.isAssignableFrom(field.getType())) {
                        StringSetConfigOption option = (StringSetConfigOption) field.get(null);
                        JsonArray array = new JsonArray();
                        ConfigOptionStorage.getStringSet(option.getKey()).forEach(array::add);
                        config.add(field.getName().toLowerCase(Locale.ROOT), array);
                    } else if (EnumConfigOption.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
                        Type generic = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        if (generic instanceof Class<?>) {
                            EnumConfigOption<?> option = (EnumConfigOption<?>) field.get(null);
                            config.addProperty(field.getName().toLowerCase(Locale.ROOT), ConfigOptionStorage.getEnumTypeless(option.getKey(), (Class<Enum<?>>) generic).name().toLowerCase(Locale.ROOT));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String jsonString = UnvotedAndShelved.GSON.toJson(config);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            System.err.println("Couldn't save Unvoted & Shelved configuration file");
            e.printStackTrace();
        }
    }
}
