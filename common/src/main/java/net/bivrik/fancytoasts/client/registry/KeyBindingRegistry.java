package net.bivrik.fancytoasts.client.registry;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.utility.KeyBinding;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KeyBindingRegistry {
    private final static Logger LOGGER = Debug.getLogger(KeyBindingRegistry.class);

    private final static Map<String, KeyBinding> KEY_BINDINGS = new HashMap<>();

    public static void register(String name, int keyCode, KeyBinding.keyExecutor executor) {
        String translatableName = "key." + Constants.MOD_ID + "." + name;

        if (isRegistered(translatableName)) {
            LOGGER.warn("{} already exists, could not add", translatableName);
            return;
        }

        KeyMapping key = new KeyMapping(translatableName, keyCode, KeyMapping.CATEGORY_MISC);
        KeyBinding bind = new KeyBinding(key, executor);

        KEY_BINDINGS.put(translatableName, bind);

        LOGGER.info("Registered: {}", translatableName);
    }

    public static boolean isRegistered(String keyName) {
        return KEY_BINDINGS.containsKey(keyName);
    }

    public static KeyBinding[] keyBindings() {
        return KEY_BINDINGS.values().toArray(new KeyBinding[0]);
    }

    public static KeyMapping[] getExtendedKeys(KeyMapping[] builtinKeys) {
        KeyBinding[] modKeyBindings = keyBindings();
        int builtinKeysLength = builtinKeys.length;
        int modKeyBindingsLength = modKeyBindings.length;

        KeyMapping[] extendedKeys = Arrays.copyOf(builtinKeys, builtinKeysLength + modKeyBindingsLength);
        for (int i = 0; i < modKeyBindingsLength; i++) {
            extendedKeys[builtinKeysLength + i] = modKeyBindings[i].key();
        }

        LOGGER.info("Returned extended keys");
        return extendedKeys;
    }
}
