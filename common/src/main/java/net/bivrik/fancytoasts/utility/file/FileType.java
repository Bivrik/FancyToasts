package net.bivrik.fancytoasts.utility.file;

public enum FileType {
    PNG(".png"),
    JSON(".json");

    private final String name;

    FileType(String name) {
        this.name = name;
    }

    public String get() {
        return name;
    }
}
