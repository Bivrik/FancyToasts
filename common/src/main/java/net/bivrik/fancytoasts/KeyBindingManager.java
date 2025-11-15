package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.KeyBinding;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class KeyBindingManager {
    private final static Logger LOGGER = Debug.getLogger(KeyBindingManager.class);

    public final List<KeyBinding> FANCY_TOASTS_KEYS = new ArrayList<>();

    public void registerKey(String name, int keyCode, KeyBinding.keyExecutor executor) {
        name = "key." + Constants.MOD_ID + "." + name;

        KeyMapping key = new KeyMapping(name, keyCode, KeyMapping.Category.MISC);
        KeyBinding bind = new KeyBinding(key, executor);

        if (FANCY_TOASTS_KEYS.contains(bind)) {
            LOGGER.warn("{} already exists, could not add", name);
            return;
        }

        FANCY_TOASTS_KEYS.add(bind);
        LOGGER.info("{} added", name);
    }

    public KeyMapping[] getUpdatedKeys(KeyMapping[] builtinKeys) {
        List<KeyMapping> keys = new ArrayList<>(List.of(builtinKeys));
        for (var keyBinding : FANCY_TOASTS_KEYS) {
            keys.add(keyBinding.key());
        }
        return keys.toArray(new KeyMapping[0]);
    }

    public void tick() {
        for (var keyBinding : FANCY_TOASTS_KEYS) {
            if (keyBinding.key().consumeClick()) {
                keyBinding.keyExecutor().execute();
            }
        }
    }
}
