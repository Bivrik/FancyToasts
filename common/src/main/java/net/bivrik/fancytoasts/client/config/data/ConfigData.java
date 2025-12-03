package net.bivrik.fancytoasts.client.config.data;

public abstract class ConfigData {
    private transient final String path;

    public ConfigData(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract boolean isValid();
    public abstract ConfigData copy();

    @Override
    public String toString() {
        return getClass().getSimpleName() + String.format("{path='%s'}", path);
    }
}
