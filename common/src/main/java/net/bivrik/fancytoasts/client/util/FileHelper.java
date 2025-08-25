package net.bivrik.fancytoasts.client.util;

import net.bivrik.fancytoasts.Debug;

import java.io.File;

public class FileHelper {
    public static String getRawName(File file) {
        return file.getName().replace(FileType.PNG, "").replace(FileType.JSON, "");
    }

    public static boolean tryCreateDir(File directory) {
        if (directory.exists()) return false;

        if (!directory.mkdir()) {
            Debug.error("Security manager does not let create directory in: " + directory.getPath());
        }

        return true;
    }
}
