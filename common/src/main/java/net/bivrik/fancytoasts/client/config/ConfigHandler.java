package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.client.config.data.ConfigData;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.utility.file.FileHelper;
import net.bivrik.fancytoasts.utility.file.Paths;

import java.io.File;
import java.util.Optional;

public class ConfigHandler {
    private static final File CONFIG_DIR = new File(Paths.CONFIG);

    private static <T extends ConfigData> T tryGetCopy(Class<T> configDataClass) {
        try {
            @SuppressWarnings("unchecked")
            T result = (T) configDataClass.getConstructor().newInstance().copy();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of " + configDataClass.getSimpleName() + " to get standard data", e);
        }
    }

    public static <T extends ConfigData> T load(Class<T> configDataClass) {
        FileHelper.tryCreateDir(CONFIG_DIR);

        T standardConfigData = tryGetCopy(configDataClass);
        String className = configDataClass.getSimpleName();
        String configPath = standardConfigData.getPath();

        File configFile = new File(configPath);

        if (!JsonHelper.isValid(configFile)) {
            Debug.error("Config file {} is not found in '{}'", className, configPath);
            return loadFallback(standardConfigData, className);
        }

        Optional<T> optionalData = JsonHelper.tryToRead(configFile, configDataClass);
        if (optionalData.isEmpty()) {
            Debug.error("Config file {} is not present", className);
            return loadFallback(standardConfigData, className);
        }

        T data = optionalData.get();
        if (data.isOutdated()) {
            Debug.error("Config file {} is outdated, version: {}/{}", className, data.getVersion(), data.getLatestVersion());
            return loadFallback(standardConfigData, className);
        }

        if (!data.isValid()) {
            Debug.error("Config file {} is not valid", className);
            return loadFallback(standardConfigData, className);
        }

        Debug.info("Successfully read config file with following content:");
        Debug.info(data.toString());

        return data;
    }

    private static <T extends ConfigData> T loadFallback(T standardConfigData, String className) {
        Debug.warn("Loaded standard data for config: {}", className);

        save(standardConfigData);
        return standardConfigData;
    }

    public static <T extends ConfigData> void save(T configData) {
        FileHelper.tryCreateDir(CONFIG_DIR);

        File configFile = new File(configData.getPath());

        if (JsonHelper.tryToWrite(configFile, configData)) {
            Debug.info("Config file saved with following content:");
            Debug.info(configData.toString());

            Managers.getConfigManager().updateConfig(configData);
        } else {
            Debug.error("Config file {} could not be saved", configData.getClass().getSimpleName());
        }
    }
}
