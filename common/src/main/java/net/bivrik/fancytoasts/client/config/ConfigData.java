package net.bivrik.fancytoasts.client.config;

public abstract class ConfigData {
    private transient final String path;

    protected ConfigData(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract boolean isValid();
    public abstract ConfigData get();

    @Override
    public String toString() {
        return getClass().getSimpleName() + String.format(" {path='%s'}", path);
    }
}
