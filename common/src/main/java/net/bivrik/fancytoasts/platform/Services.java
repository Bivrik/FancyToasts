package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

// This code is used to load a service for the current environment. Implementation of the service must be defined
// manually by including a text file in META-INF/services named with the fully qualified class displayName of the service.
// Inside the file you should write the fully qualified class displayName of the implementation to load for the platform.
public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Debug.message("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
