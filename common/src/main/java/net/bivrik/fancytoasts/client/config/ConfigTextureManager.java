package net.bivrik.fancytoasts.client.config;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.texture.DisplayData;
import net.bivrik.fancytoasts.utility.file.FileHelper;
import net.bivrik.fancytoasts.utility.file.FileType;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.bivrik.fancytoasts.client.toast.ToastTextureRegistry;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigTextureManager {
    private static final Map<ResourceLocation, Path> CONFIG_TEXTURES = new HashMap<>();

    private static final File TEXTURES_DIR = new File(Paths.CONFIG_TEXTURES);

    private static final FileFilter TEXTURE_FILES_FILTER = (file) -> {
        String name = file.getName();
        return name.endsWith(FileType.PNG.get()) || name.endsWith(FileType.JSON.get());
    };

    public static void registerInMinecraft(ResourceLocation id) {
        if (!ToastTextureRegistry.isRegistered(id)) {
            Debug.error("Could not register texture {} in minecraft texture manager", id);
            return;
        }

        try {
            NativeImage image = NativeImage.read(Files.readAllBytes(CONFIG_TEXTURES.get(id)));
            DynamicTexture dynamicTexture = new DynamicTexture(
                    () -> "config_toast_texture",
                    image
            );

            Minecraft.getInstance().getTextureManager().register(id, dynamicTexture);

            image.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unregisterFromMinecraft() {
        for (ResourceLocation id : CONFIG_TEXTURES.keySet()) {
            Minecraft.getInstance().getTextureManager().release(id);
        }
    }

    public static void reload() {
        unregisterFromMinecraft();
        CONFIG_TEXTURES.clear();
        ToastTextureRegistry.clearCustom();

        load();
    }

    public static void load() {
        if (FileHelper.tryCreateDir(TEXTURES_DIR)) {
            return;
        }

        File[] files = TEXTURES_DIR.listFiles(TEXTURE_FILES_FILTER);
        if (files == null) {
            Debug.warn("There are no files in the config texture directory");
            return;
        }

        int initialCap = files.length / 2;
        List<File> textureFiles = new ArrayList<>(initialCap);
        List<File> jsonFiles = new ArrayList<>(initialCap);

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(FileType.PNG.get())) {
                textureFiles.add(file);
            } else if (fileName.endsWith(FileType.JSON.get())) {
                jsonFiles.add(file);
            }
        }

        register(jsonFiles, textureFiles);
    }

    private static void register(List<File> jsonFiles, List<File> textureFiles) {
        for (File jsonFile : jsonFiles) {
            for (File textureFile : textureFiles) {
                if (FileHelper.getRawName(textureFile).compareTo(FileHelper.getRawName(jsonFile)) == 0) {
                    Optional<DisplayData> optionalData = JsonHelper.tryToRead(jsonFile, DisplayData.class);

                    if (optionalData.isPresent()) {
                        DisplayData data = optionalData.get();
                        ResourceLocation id = ResourceLocations.of(textureFile.getPath().replace("\\", "/").replaceFirst("./", ""));

                        if (ToastTextureRegistry.register(id, null, data.getName().getString(), data.getAuthor().getString(), data.getDescription().getString())) {
                            CONFIG_TEXTURES.put(id, textureFile.toPath());
                        }
                    }
                    else {
                        Debug.error("Texture data is outdated or corrupted! File: {}", jsonFile.getAbsolutePath());
                    }
                }
            }
        }
    }
}
