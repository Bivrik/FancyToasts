package net.bivrik.fancytoasts.core.manager;

import com.mojang.blaze3d.platform.NativeImage;
import net.bivrik.fancytoasts.client.config.JsonHelper;
import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.IManager;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.event.ToastConfigDataEvent;
import net.bivrik.fancytoasts.platform.utility.Identifiers;
import net.bivrik.fancytoasts.utility.file.FileHelper;
import net.bivrik.fancytoasts.utility.file.FileType;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class CustomTextureManager implements IManager {
    private static final Logger LOGGER = Debug.getLogger(CustomTextureManager.class);

    private static final File TEXTURES_DIR = new File(Paths.CONFIG_TEXTURES);
    private static final FileFilter TEXTURE_FILES_FILTER = (file) -> {
        String name = file.getName();
        return name.endsWith(FileType.PNG.get()) || name.endsWith(FileType.JSON.get());
    };

    private final Map<Identifier, DisplayData> customTextures = new HashMap<>();
    private final List<Identifier> registeredInMinecraft = new ArrayList<>();
    private final Map<Identifier, List<FancyAdvancementToast>> beingUsed = new HashMap<>();

    private TextureManager textureManager;
    private ToastConfigData toastConfigData;

    @Override
    public void onMinecraftInit(Minecraft minecraft) {
        textureManager = minecraft.getTextureManager();
        toastConfigData = Managers.getConfigManager().getToastConfigData();
        Managers.getEventManager().subscribeToEvent(ToastConfigDataEvent.class, this::onToastConfigDataChanged);

        load();
        registerInMainRegistry();

        Identifier textureId = toastConfigData.getTextureId();
        if (textureId.getPath().contains(Constants.CONFIG)) {
            registerInMinecraft(textureId);
        }
    }

    private void onToastConfigDataChanged(ToastConfigDataEvent event) {
        toastConfigData = event.toastConfigData();
    }

    public void addBeingUsed(Identifier id, FancyAdvancementToast toast) {
        if (!id.getPath().contains(Constants.CONFIG)) {
            return;
        }

        beingUsed.computeIfAbsent(id, list -> new ArrayList<>()).add(toast);
        LOGGER.info("Added to: {}, toast: {}; total size: {}", id, toast, beingUsed.get(id).size());
    }

    public void removeBeingUsed(FancyAdvancementToast toast) {
        Identifier id = null;
        for (Map.Entry<Identifier, List<FancyAdvancementToast>> entry : beingUsed.entrySet()) {
            if (entry.getValue().contains(toast)) {
                id = entry.getKey();
                break;
            }
        }

        List<FancyAdvancementToast> toasts = beingUsed.get(id);
        if (toasts != null) {
            toasts.remove(toast);
            LOGGER.info("Removed from: {}, toast: {}; total size: {}", id, toast, toasts.size());
            if (toasts.isEmpty()) {
                beingUsed.remove(id);
                LOGGER.warn("Removed texture from cash: {}", id);
                Identifier currentId = toastConfigData.getTextureId();
                if (currentId != id) {
                    releaseTextureFromMinecraft(id);
                }
            }
        }
    }

    public void registerInMinecraft(Identifier id) {
        if (isRegisteredMinecraft(id)) {
            return;
        }

        if (!TextureRegistry.isRegistered(id)) {
            LOGGER.error("Could not register in Minecraft, because it does not exist in Texture Registry: {}", id);
            return;
        }

        try {
            NativeImage image = NativeImage.read(Files.readAllBytes(getFileFromId(id).toPath()));
            String dynamicTextureName = id.toLanguageKey().replace(FileType.PNG.get(), "").replace("/", "_").replace(".", "-");
            DynamicTexture dynamicTexture = new DynamicTexture(() -> dynamicTextureName, image);

            textureManager.register(id, dynamicTexture);
            registeredInMinecraft.add(id);

            image.close();
            LOGGER.info("Registered in Minecraft: {}; {}", id, dynamicTextureName);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while registering custom texture in Minecraft: ", e);
        }
    }

    public void releaseTextureFromMinecraft(Identifier id) {
        if (!registeredInMinecraft.contains(id)) {
            return;
        }

        textureManager.release(id);
        registeredInMinecraft.remove(id);
        LOGGER.info("Released from Minecraft: {}", id);
    }

    public void releaseUnusedTexturesFromMinecraft() {
        new ArrayList<>(registeredInMinecraft).forEach(id -> {
            if (!beingUsed.containsKey(id)) releaseTextureFromMinecraft(id);
        });
    }

    public boolean isRegisteredMinecraft(Identifier id) {
        return registeredInMinecraft.contains(id);
    }

    public void clear() {
        Identifier currentId = toastConfigData.getTextureId();
        if (currentId.getPath().contains(Constants.CONFIG)) {
            registeredInMinecraft.forEach(id -> {
                if (currentId != id) textureManager.release(id);
            });
        } else {
            registeredInMinecraft.forEach(id -> {
                textureManager.release(id);
            });
        }

        beingUsed.clear();
        registeredInMinecraft.clear();
    }

    public void reload() {
        load();
        cleanUpFromMainRegistry();
        registerInMainRegistry();
    }

    public void load() {
        if (FileHelper.tryCreateDirectory(TEXTURES_DIR)) {
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

        Map<String, File> texturesMap = textureFiles.stream().collect(Collectors.toMap(FileHelper::getRawName, textureFile -> textureFile));

        register(texturesMap, jsonFiles);
    }

    private void register(Map<String, File> texturesMap, List<File> jsonFiles) {
        customTextures.clear();

        for (File jsonFile : jsonFiles) {
            File textureFile = texturesMap.get(FileHelper.getRawName(jsonFile));

            if (textureFile != null) {
                Optional<DisplayData.DTO> optionalDataDTO = JsonHelper.tryToRead(jsonFile, DisplayData.DTO.class);

                if (optionalDataDTO.isPresent()) {
                    DisplayData data = new DisplayData(optionalDataDTO.get());
                    Identifier id = getIdFromFile(textureFile);

                    customTextures.put(id, data);
                    LOGGER.info("Added: {}", id);
                } else {
                    LOGGER.warn("Json data is outdated or corrupted! File: {}", jsonFile.getAbsolutePath());
                }
            }
        }
    }

    private void cleanUpFromMainRegistry() {
        TextureRegistry.getCustomIds().forEach(id -> {
            if (!customTextures.containsKey(id)) {
                TextureRegistry.unregister(id);
            }
        });
    }

    private void registerInMainRegistry() {
        customTextures.forEach((id, data) -> {
            if (!TextureRegistry.isRegistered(id)) {
                TextureRegistry.register(id, data);
            }
        });
    }

    // Make it more constant
    // Please, don't forget
    // Uugh God
    private Identifier getIdFromFile(File file) {
        String rawPath = file.getPath().replace("\\", "/").replaceFirst("./config/fancytoasts", "config");
        Debug.warn(rawPath);
        return Identifiers.of(rawPath);
    }

    private File getFileFromId(Identifier id) {
        String rawPath = id.getPath().replaceFirst("config", "./config/fancytoasts");
        Debug.warn(rawPath);
        return new File(rawPath);
    }
}
