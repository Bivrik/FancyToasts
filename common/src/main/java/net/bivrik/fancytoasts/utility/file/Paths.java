package net.bivrik.fancytoasts.utility.file;

import net.bivrik.fancytoasts.core.Constants;

import java.nio.file.Path;

public class Paths {
    public static final String CONFIG = "./config/" + Constants.MOD_ID + "/";
    public static final String CONFIG_TEXTURES = CONFIG + "textures/";
    public static final String TOAST_CONFIG_FILE = CONFIG + "toast.json";
    public static final String GENERAL_CONFIG_FILE = CONFIG + "general.json";
    public static final String TOASTS_FILTERING_FILE = CONFIG + "toast_filtering.json";
    public static final String CREDITS = "./bivrik/";
    public static final String MOD_CREDITS = CREDITS + "mods/";
    public static final String COMMON_CREDITS_FILE = CREDITS + "common.json";
    public static final String MOD_CREDITS_FILE = MOD_CREDITS + "fancytoasts.json";

    public static Path actualPath(String string) {
        return Path.of(string);
    }
}
