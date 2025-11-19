package net.bivrik.fancytoasts.client;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.platform.ITickableManager;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;

import java.util.*;

public class KeyBindingManager implements ITickableManager {
    private final static Logger LOGGER = Debug.getLogger(KeyBindingManager.class);

    private final List<KeyBinding> moddedKeyBindings = new ArrayList<>();
    private final Map<String, KeyBinding> moddedKeyBindingsMap = new HashMap<>();

    public void registerKey(String name, int keyCode, KeyBinding.keyExecutor executor) {
        String translatableName = "key." + Constants.MOD_ID + "." + name;

        if (moddedKeyBindingsMap.containsKey(translatableName)) {
            LOGGER.warn("{} already exists, could not add", translatableName);
            return;
        }

        KeyMapping key = new KeyMapping(translatableName, keyCode, KeyMapping.Category.MISC);
        KeyBinding bind = new KeyBinding(key, executor);

        moddedKeyBindings.add(bind);
        moddedKeyBindingsMap.put(translatableName, bind);

        LOGGER.info("Registered: {}", translatableName);
    }

    public KeyMapping[] getModdedKeys(KeyMapping[] builtinKeys) {
        int oldLength = builtinKeys.length;
        KeyMapping[] moddedKeys = Arrays.copyOf(builtinKeys, oldLength + moddedKeyBindings.size());
        for (int i = 0; i < moddedKeyBindings.size(); i++) {
            moddedKeys[oldLength + i] = moddedKeyBindings.get(i).key();
        }
        LOGGER.info("Returned modded keys");
        return moddedKeys;
    }

    @Override
    public void onTick() {
        for (KeyBinding keyBinding : moddedKeyBindings) {
            if (keyBinding.key().consumeClick()) {
                keyBinding.keyExecutor().execute();
            }
        }
    }
}
