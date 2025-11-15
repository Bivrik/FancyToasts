package net.bivrik.fancytoasts.client;

import net.bivrik.fancytoasts.client.config.ConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.GeneralConfigData;
import net.bivrik.fancytoasts.client.config.ToastConfigData;

import java.util.HashMap;

public class ConfigManager {
    private final HashMap<Class<? extends ConfigData>, ConfigData> CONFIGS = new HashMap<>();

    public void loadConfigs() {
        loadConfig(ToastConfigData.class);
        loadConfig(GeneralConfigData.class);
    }

    public <T extends ConfigData> void loadConfig(Class<T> configDataClass) {
        CONFIGS.remove(configDataClass);
        CONFIGS.put(configDataClass, ConfigHandler.load(configDataClass));
    }

    public <T extends ConfigData> void updateConfig(T configData) {
        CONFIGS.remove(configData.getClass());
        CONFIGS.put(configData.getClass(), configData);
    }

    public GeneralConfigData getGeneralConfig() {
        return (GeneralConfigData) CONFIGS.get(GeneralConfigData.class).get();
    }

    public ToastConfigData getToastConfig() {
        return (ToastConfigData) CONFIGS.get(ToastConfigData.class).get();
    }
}
