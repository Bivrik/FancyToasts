package net.bivrik.fancytoasts.core;

import net.bivrik.fancytoasts.core.manager.*;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Managers {
    private static final Map<Class<? extends IManager>, IManager> MANAGERS = new HashMap<>();
    private static final List<ITickableManager> TICKABLE_MANAGERS = new ArrayList<>();

    public static void init() {
        // Main managers (first!)
        registerManager(ConfigManager.class, new ConfigManager());
        registerManager(EventManager.class, new EventManager());

        // Other managers
        registerManager(KeyBindingManager.class, new KeyBindingManager());
        registerManager(SplashManager.class, new SplashManager());
        registerManager(CreditsManager.class, new CreditsManager());
        registerManager(CustomTextureManager.class, new CustomTextureManager());
        registerManager(ToastManager.class, new ToastManager());
    }

    public static void onModInit() {
        MANAGERS.values().forEach(IManager::onModInit);
    }

    public static void onMinecraftInit(Minecraft minecraft) {
        MANAGERS.values().forEach((managerProvider) -> {
            managerProvider.onMinecraftInit(minecraft);
        });
    }

    public static void onTick() {
        for (ITickableManager tickableManager : TICKABLE_MANAGERS) {
            tickableManager.onTick();
        }
    }

    private static <T extends IManager> void registerManager(Class<T> managerClass, T managerInstance) {
        MANAGERS.put(managerClass, managerInstance);

        if (managerInstance instanceof ITickableManager tickableManager) {
            TICKABLE_MANAGERS.add(tickableManager);
        }
    }

    private static <T extends IManager> T get(Class<T> managerClass) {
        @SuppressWarnings("unchecked")
        T result = (T) MANAGERS.get(managerClass);
        if (result == null) {
            throw new IllegalStateException("Trying to access unregistered manager: " + managerClass.getSimpleName());
        }
        return result;
    }

    public static ConfigManager getConfigManager() {
        return get(ConfigManager.class);
    }

    public static EventManager getEventManager() {
        return get(EventManager.class);
    }

    public static KeyBindingManager getKeyBindingManager() {
        return get(KeyBindingManager.class);
    }

    public static SplashManager getSplashManager() {
        return get(SplashManager.class);
    }

    public static CreditsManager getCreditsManager() {
        return get(CreditsManager.class);
    }

    public static CustomTextureManager getCustomTextureManager() {
        return get(CustomTextureManager.class);
    }

    /**
     * Some mods trigger Minecraft's ToastManager {@code update()} or {@code render()} on Minecraft initialization. Therefore, it can return null during this phase to avoid immediate crash
     * @return {@link ToastManager}
     */
    public static @Nullable ToastManager getAdvancementToastManager() {
        try {
            return get(ToastManager.class);
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
