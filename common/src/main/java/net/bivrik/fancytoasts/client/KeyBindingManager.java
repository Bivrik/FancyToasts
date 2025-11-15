package net.bivrik.fancytoasts.client;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class KeyBindingManager {
    private final static Logger LOGGER = Debug.getLogger(KeyBindingManager.class);

    public final List<KeyBinding> KEY_BINDINGS = new ArrayList<>();

    public void registerKey(String name, int keyCode, KeyBinding.keyExecutor executor) {
        name = "key." + Constants.MOD_ID + "." + name;

        KeyMapping key = new KeyMapping(name, keyCode, KeyMapping.Category.MISC);
        KeyBinding bind = new KeyBinding(key, executor);

        if (KEY_BINDINGS.contains(bind)) {
            LOGGER.warn("{} already exists, could not add", name);
            return;
        }

        KEY_BINDINGS.add(bind);
        LOGGER.info("Registered {}", name);
    }

    public KeyMapping[] getUpdatedKeys(KeyMapping[] builtinKeys) {
        List<KeyMapping> keys = new ArrayList<>(List.of(builtinKeys));
        for (var keyBinding : KEY_BINDINGS) {
            keys.add(keyBinding.key());
        }
        return keys.toArray(new KeyMapping[0]);
    }

    public void tick() {
        for (var keyBinding : KEY_BINDINGS) {
            if (keyBinding.key().consumeClick()) {
                keyBinding.keyExecutor().execute();
            }
        }
    }
}
