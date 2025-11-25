package net.bivrik.fancytoasts.utility.file;

import net.bivrik.fancytoasts.core.Constants;

import java.nio.file.Path;

public class Paths {
    public static final String CONFIG = "./config/" + Constants.MOD_ID + "/";
    public static final String CONFIG_TEXTURES = CONFIG + "textures/";
    public static final String TOAST_CONFIG_FILE = CONFIG + "toast.json";
    public static final String GENERAL_CONFIG_FILE = CONFIG + "general.json";

    public static Path actualPath(String string) {
        return Path.of(string);
    }
}
