package net.bivrik.fancytoasts.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.config.JsonHelper;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.platform.IManager;
import net.bivrik.fancytoasts.utility.file.FileHelper;
import net.bivrik.fancytoasts.utility.file.FileType;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.bivrik.fancytoasts.client.registries.TextureRegistry;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CustomTextureManager implements IManager {
    private static final Logger LOGGER = Debug.getLogger(CustomTextureManager.class);
    private static final Map<ResourceLocation, Path> CUSTOM_TEXTURES = new HashMap<>();

    private static final File TEXTURES_DIR = new File(Paths.CONFIG_TEXTURES);
    private static final FileFilter TEXTURE_FILES_FILTER = (file) -> {
        String name = file.getName();
        return name.endsWith(FileType.PNG.get()) || name.endsWith(FileType.JSON.get());
    };

    private TextureManager textureManager;

    @Override
    public void onMinecraftInit(Minecraft minecraft) {
        textureManager = minecraft.getTextureManager();

        load();
    }

    public void registerInMinecraft(ResourceLocation id) {
        if (!TextureRegistry.isRegistered(id)) {
            LOGGER.error("Could not register {} in Minecraft, because it does not exist in Texture Registry", id);
            return;
        }

        try {
            NativeImage image = NativeImage.read(Files.readAllBytes(CUSTOM_TEXTURES.get(id)));
            DynamicTexture dynamicTexture = new DynamicTexture(() -> "custom_fancytoasts_texture", image);

            textureManager.register(id, dynamicTexture);

            image.close();
            LOGGER.info("Registered {} in Minecraft", id);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while registering custom texture: ", e);
        }
    }

    public void unregisterFromMinecraft(ResourceLocation id) {
        textureManager.release(id);
        LOGGER.info("Unregistered {} from Minecraft", id);
    }

    public void releaseTexturesFromMinecraft() {
        for (ResourceLocation id : CUSTOM_TEXTURES.keySet()) {
            unregisterFromMinecraft(id);
        }
    }

    public void clear() {
        CUSTOM_TEXTURES.clear();
        TextureRegistry.clearCustom();
    }

    public void reload() {
        clear();
        load();
    }

    public void load() {
        if (FileHelper.tryCreateDir(TEXTURES_DIR)) {
            return;
        }

        File[] files = TEXTURES_DIR.listFiles(TEXTURE_FILES_FILTER);
        if (files == null) {
            LOGGER.info("No custom textures");
            return;
        }

        int initialCap = Math.max(0, files.length / 2 - 1);
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

    private void register(List<File> jsonFiles, List<File> textureFiles) {
        for (File jsonFile : jsonFiles) {
            for (File textureFile : textureFiles) {
                if (FileHelper.getRawName(textureFile).compareTo(FileHelper.getRawName(jsonFile)) == 0) {
                    Optional<DisplayData.DTO> optionalData = JsonHelper.tryToRead(jsonFile, DisplayData.DTO.class);

                    if (optionalData.isPresent()) {
                        DisplayData data = new DisplayData(optionalData.get());
                        ResourceLocation id = ResourceLocations.of(textureFile.getPath().replace("\\", "/").replaceFirst("./", ""));

                        if (TextureRegistry.register(id, data)) {
                            CUSTOM_TEXTURES.put(id, textureFile.toPath());
                        }
                    }
                    else {
                        LOGGER.warn("Data is outdated or corrupted! File: {}", jsonFile.getAbsolutePath());
                    }
                }
            }
        }
    }
}
