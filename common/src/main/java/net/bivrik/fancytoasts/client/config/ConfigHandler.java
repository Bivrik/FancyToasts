package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.util.FileHelper;
import net.bivrik.fancytoasts.client.util.Paths;

import java.io.File;
import java.util.Optional;

public class ConfigHandler {
    private static final File CONFIG_DIR = new File(Paths.CONFIG);

    private static <T extends ConfigData> T tryGetInstance(Class<T> configDataClass) {
        try {
            return configDataClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of " + configDataClass.getSimpleName() + " to get standard data", e);
        }
    }

    public static <T extends ConfigData> T load(Class<T> configDataClass) {
        FileHelper.tryCreateDir(CONFIG_DIR);

        String className = configDataClass.getSimpleName();
        T standardConfigData = tryGetInstance(configDataClass);

        File configFile = new File(standardConfigData.getPath());

        if (JsonHelper.isValid(configFile)) {
            Optional<T> optionalData = JsonHelper.tryToRead(configFile, configDataClass);

            if (optionalData.isPresent()) {
                T data = optionalData.get();

                if (data.isValid()) {
                Debug.info("Successfully read config file with following content:");
                Debug.info(data.toString());

                return data;
                }
            }

            Debug.error("Config file {} is not valid or outdated", className);
        } else {
            Debug.error("Config file {} is not found in {}", className, standardConfigData.getPath());
        }

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

            Common.getConfigManager().updateConfig(configData);
        } else {
            Debug.error("Config file {} could not be saved", configData.getClass().getSimpleName());
        }
    }
}
