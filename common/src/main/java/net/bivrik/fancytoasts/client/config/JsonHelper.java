package net.bivrik.fancytoasts.client.config;

import com.google.gson.*;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Optional;

public class JsonHelper {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdapter())
            .create();


    /**
     * Adapter for correct working Json files: ResourceLocation
     */
    private static class ResourceLocationAdapter implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return ResourceLocations.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    /**
     * Access to {@code GSON} for specific cases
     * @return {@code GSON}
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Reads a json from file.
     * @param jsonFile file to read from.
     * @param toClass class that {@code jsonFile} has to be transformed to.
     * @param <T> class reference.
     * @return optional data from {@code jsonFile} in given {@code toClass} class.
     */
    public static <T> Optional<T> tryToRead(File jsonFile, Class<T> toClass) {
        try (FileReader reader = new FileReader(jsonFile)) {
            return Optional.of(GSON.fromJson(reader, toClass));
        } catch (Exception e) {
            Debug.error("Could not read json file {}: {}", jsonFile.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    public static boolean tryToWrite(File jsonFile, Object fromClass) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            GSON.toJson(fromClass, writer);
        } catch (Exception e) {
            Debug.error("Could not write json file {}: {}", jsonFile.getName(), e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean isValid(File jsonFile) {
        return jsonFile.exists() && jsonFile.length() > 0;
    }
}
