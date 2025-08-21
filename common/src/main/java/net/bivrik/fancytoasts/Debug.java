package net.bivrik.fancytoasts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Debug {
    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_NAME);

    public static void message(String msg) {
        LOGGER.info(msg);
    }
    public static void message(String msg, Object... args) {
        LOGGER.info(msg, args);
    }

    public static void warn(String msg) {
        LOGGER.warn(msg);
    }
    public static void warn(String msg, Object... args) {
        LOGGER.warn(msg, args);
    }

    public static void error(String msg) {
        LOGGER.error(msg);
    }
    public static void error(String msg, Object... args) {
        LOGGER.error(msg, args);
    }
    public static void error(String msg, Throwable e) {
        LOGGER.error(msg, e);
    }
}
