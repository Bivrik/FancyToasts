package net.bivrik.fancytoasts.client.config.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class ConfigData {
    private transient final String path;
    private int version;

    protected ConfigData(String path) {
        this.path = path;
        this.version = 0;
    }

    protected <T extends ConfigData> T withLatestVersion() {
        this.version = getLatestVersion();
        @SuppressWarnings("unchecked")
        T result = (T) this;
        return result;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public boolean isOutdated() {
        return version != getLatestVersion();
    }

    public boolean isValid() {
        return true;
    }

    public abstract int getLatestVersion();
    public abstract ConfigData copy();

    protected final ToStringBuilder getBaseToStringBuilder() {
        return new ToStringBuilder(this).append("path", path).append("version", version);
    }

    @Override
    public String toString() {
        return getBaseToStringBuilder().toString();
    }
}
