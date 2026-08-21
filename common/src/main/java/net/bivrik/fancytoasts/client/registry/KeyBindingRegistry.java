package net.bivrik.fancytoasts.client.registry;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.utility.KeyBinding;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class KeyBindingRegistry {
    private final static Logger LOGGER = Debug.getLogger(KeyBindingRegistry.class);

    private final static Map<String, KeyBinding> KEY_BINDINGS = new HashMap<>();

    public static void register(String keyName, int keyCode, KeyBinding.keyExecutor executor) {
        String name = "key." + Constants.MOD_ID + "." + keyName;

        if (isRegistered(name)) {
            LOGGER.warn("Could not add {} because it already exists", name);
            return;
        }

        KeyMapping key = new KeyMapping(name, keyCode, KeyMapping.CATEGORY_MISC);
        KeyBinding bind = new KeyBinding(key, executor);

        KEY_BINDINGS.put(name, bind);

        LOGGER.info("Registered {}", name);
    }

    public static boolean isRegistered(String name) {
        return KEY_BINDINGS.containsKey(name);
    }

    public static KeyBinding[] getModKeyBindings() {
        return KEY_BINDINGS.values().toArray(new KeyBinding[0]);
    }

    public static KeyMapping[] getExtendedKeys(KeyMapping[] registeredKeys) {
        Set<String> registeredKeyNames = new HashSet<>(registeredKeys.length);
        for (KeyMapping key : registeredKeys) {
            registeredKeyNames.add(key.getName());
        }

        List<KeyMapping> keysToAdd = KEY_BINDINGS.values().stream()
                .map(KeyBinding::key)
                .filter(key -> !registeredKeyNames.contains(key.getName()))
                .toList();

        KeyMapping[] extendedKeys = Arrays.copyOf(registeredKeys, registeredKeys.length + keysToAdd.size());
        for (int i = 0; i < keysToAdd.size(); i++) {
            extendedKeys[registeredKeys.length + i] = keysToAdd.get(i);
        }

        LOGGER.info("Returned extended keys for options");
        return extendedKeys;
    }
}
