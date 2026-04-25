package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.services.*;
import org.slf4j.Logger;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public class Services {
    private static final Logger LOGGER = Debug.getLogger(Services.class);

    // Must-have service. Platform has to be loaded
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    // Optional ones
    private static final Map<Class<?>, Supplier<?>> FALLBACKS = Map.of(
            IJadeHelper.class, () -> new IJadeHelper() {},
            IFTBQuestsHelper.class, () -> new IFTBQuestsHelper() {},
            IAetherHelper.class, () -> new IAetherHelper() {},
            IDawnEraHelper.class, () -> new IDawnEraHelper() {}
    );

    public static final IJadeHelper JADE = loadOptional(IJadeHelper.class);
    public static final IFTBQuestsHelper FTB_QUESTS = loadOptional(IFTBQuestsHelper.class);
    public static final IAetherHelper AETHER_HELPER = loadOptional(IAetherHelper.class);
    public static final IDawnEraHelper DAWN_ERA_HELPER = loadOptional(IDawnEraHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOGGER.info("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    public static <T> T loadOptional(Class<T> clazz) {
        try {
            return load(clazz);
        } catch (NullPointerException e) {
            Supplier<?> fallback = FALLBACKS.get(clazz);
            if (fallback == null) {
                throw new RuntimeException("No service implementation of fallback found for optional " + clazz);
            }
            @SuppressWarnings("unchecked")
            T result = (T) fallback.get();
            LOGGER.info("Loaded fallback {} for service {}", result, clazz);
            return result;
        }
    }
}
