package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.client.config.data.ConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.bivrik.fancytoasts.core.IManager;

import java.util.HashMap;

public class ConfigManager implements IManager {
    private final HashMap<Class<? extends ConfigData>, ConfigData> configs = new HashMap<>();

    @Override
    public void onModInit() {
        loadConfig(ToastConfigData.class);
        loadConfig(GeneralConfigData.class);
    }

    private <T extends ConfigData> void loadConfig(Class<T> configDataClass) {
        configs.remove(configDataClass);
        configs.put(configDataClass, ConfigHandler.load(configDataClass));
    }

    public <T extends ConfigData> void updateConfig(T configData) {
        configs.replace(configData.getClass(), configData);
    }

    private <T extends ConfigData> T getConfig(Class<T> configDataClass) {
        @SuppressWarnings("unchecked")
        T result = (T) configs.get(configDataClass).copy();
        if (result == null) {
            throw new IllegalStateException("Trying to access unregistered config: " + configDataClass.getSimpleName());
        }
        return result;
    }

    public GeneralConfigData getGeneralConfigData() {
        return getConfig(GeneralConfigData.class);
    }

    public ToastConfigData getToastConfigData() {
        return getConfig(ToastConfigData.class);
    }
}
