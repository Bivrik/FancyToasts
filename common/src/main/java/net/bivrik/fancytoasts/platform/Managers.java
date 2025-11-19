package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.client.ConfigManager;
import net.bivrik.fancytoasts.client.CustomTextureManager;
import net.bivrik.fancytoasts.client.KeyBindingManager;
import net.bivrik.fancytoasts.client.toast.AdvancementToastManager;
import net.bivrik.fancytoasts.client.ui.CreditsManager;
import net.bivrik.fancytoasts.client.ui.SplashManager;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Managers {
    private static final Map<Class<? extends IManager>, IManager> MANAGERS = new HashMap<>();
    private static final List<ITickableManager> TICKABLE_MANAGERS = new ArrayList<>();

    public static void init() {
        // Config Manager has to be registered first
        registerManager(ConfigManager.class, new ConfigManager());

        // Other managers
        registerManager(KeyBindingManager.class, new KeyBindingManager());
        registerManager(SplashManager.class, new SplashManager());
        registerManager(CreditsManager.class, new CreditsManager());
        registerManager(CustomTextureManager.class, new CustomTextureManager());
        registerManager(AdvancementToastManager.class, new AdvancementToastManager());
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

    public static ConfigManager configManager() {
        return get(ConfigManager.class);
    }

    public static KeyBindingManager keyBindingManager() {
        return get(KeyBindingManager.class);
    }

    public static SplashManager splashManager() {
        return get(SplashManager.class);
    }

    public static CreditsManager creditsManager() {
        return get(CreditsManager.class);
    }

    public static CustomTextureManager customTextureManager() {
        return get(CustomTextureManager.class);
    }

    public static AdvancementToastManager advancementToastManager() {
        return get(AdvancementToastManager.class);
    }
}
