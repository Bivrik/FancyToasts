package net.bivrik.fancytoasts.client.config;

import com.google.gson.*;
import net.bivrik.fancytoasts.Debug;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

public class JsonHelper {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdapter())
            .create();

    // Gson adapters
    private static class ResourceLocationAdapter implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return ResourceLocation.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    // Other
    public static <T> Optional<T> tryToRead(File jsonFile, Class<T> toClass) {
        try (FileReader reader = new FileReader(jsonFile)) {
            return Optional.of(GSON.fromJson(reader, toClass));
        } catch (IOException e) {
            Debug.error("Could not read json file {}: {}", jsonFile.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    public static boolean tryToWrite(File jsonFile, Object fromClass) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            GSON.toJson(fromClass, writer);
        } catch (IOException e) {
            Debug.error("Could not write json file {}: {}", jsonFile.getName(), e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean isValid(File jsonFile) {
        return jsonFile.exists() && jsonFile.length() > 0;
    }
}
