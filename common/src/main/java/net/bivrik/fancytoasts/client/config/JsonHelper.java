package net.bivrik.fancytoasts.client.config;

import com.google.gson.*;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.Identifiers;
import net.minecraft.resources.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Optional;

public class JsonHelper {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Identifier.class, new IdentifierAdapter())
            .create();

    private static class IdentifierAdapter implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {
        @Override
        public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Identifiers.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    public static <T> Optional<T> tryToRead(File jsonFile, Class<T> classReference) {
        try (FileReader reader = new FileReader(jsonFile)) {
            T data = GSON.fromJson(reader, classReference);
            return Optional.of(data);
        } catch (Exception e) {
            Debug.error("Could not read json file {}: {}", jsonFile.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    public static boolean tryToWrite(File jsonFile, Object data) {
        return tryToWrite(GSON, jsonFile, data);
    }

    public static boolean tryToWrite(Gson gson, File jsonFile, Object data) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(data, writer);
            return true;
        } catch (Exception e) {
            Debug.error("Could not write json file {}: {}", jsonFile.getName(), e.getMessage());
            return false;
        }
    }
}
