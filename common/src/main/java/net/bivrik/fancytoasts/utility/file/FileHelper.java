package net.bivrik.fancytoasts.utility.file;

import net.bivrik.fancytoasts.core.Debug;

import java.io.File;

public class FileHelper {
    public static String getRawName(File file) {
        return file.getName().replace(FileType.PNG.get(), "").replace(FileType.JSON.get(), "");
    }

    public static boolean tryCreateDirectory(File directory) {
        if (directory.exists()) return false;

        if (!directory.mkdir()) {
            Debug.error("Security manager does not let create directory '{}'", directory.getPath());
            return false;
        }

        return true;
    }
}
