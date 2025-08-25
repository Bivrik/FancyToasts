package net.bivrik.fancytoasts.client.config;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureData;
import net.bivrik.fancytoasts.client.util.FileHelper;
import net.bivrik.fancytoasts.client.util.FileType;
import net.bivrik.fancytoasts.client.util.Paths;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
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
    private static final Map<ResourceLocation, Path> CONFIG_TEXTURES_CASH = new HashMap<>();

    private static final File TEXTURES_DIR = new File(Paths.CONFIG_TEXTURES);

    private static final FileFilter TEXTURE_FILES_FILTER = (file) -> {
        String name = file.getName();
        return name.endsWith(FileType.PNG) || name.endsWith(FileType.JSON);
    };

    public static void registerInMinecraft(ResourceLocation id) {
        if (!ToastTextureRegistry.isRegistered(id)) {
            Debug.error("Could not register texture {} in minecraft texture manager", id);
            return;
        }

        try {
            NativeImage image = NativeImage.read(Files.readAllBytes(CONFIG_TEXTURES_CASH.get(id)));
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

    public static void unregisterFromMinecraft(ResourceLocation id) {
        Minecraft.getInstance().getTextureManager().release(id);
    }

    public static void reload() {
        CONFIG_TEXTURES_CASH.clear();
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
            if (fileName.endsWith(FileType.PNG)) {
                textureFiles.add(file);
            } else if (fileName.endsWith(FileType.JSON)) {
                jsonFiles.add(file);
            }
        }

        register(jsonFiles, textureFiles);
    }

    private static void register(List<File> jsonFiles, List<File> textureFiles) {
        for (File jsonFile : jsonFiles) {
            for (File textureFile : textureFiles) {
                if (FileHelper.getRawName(textureFile).compareTo(FileHelper.getRawName(jsonFile)) == 0) {
                    Optional<ToastTextureData> optionalData = JsonHelper.tryToRead(jsonFile, ToastTextureData.class);

                    if (optionalData.isPresent()) {
                        ToastTextureData data = optionalData.get();
                        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, textureFile.getPath().replace("\\", "/").replaceFirst("./", ""));

                        if (ToastTextureRegistry.register(id, null, data.name().getString(), data.author().getString())) {
                            CONFIG_TEXTURES_CASH.put(id, textureFile.toPath());
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
