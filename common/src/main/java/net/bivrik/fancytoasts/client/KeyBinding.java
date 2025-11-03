package net.bivrik.fancytoasts.client;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class KeyBinding {
    public KeyBinding() {}

    public record keyHolder(KeyMapping key, keyExecutor executor) {}

    public interface keyExecutor {
        void execute();
    }

    public final static List<keyHolder> FANCY_TOASTS_KEYS = new ArrayList<>();

    public static void registerKey(String name, int key, keyExecutor executor) {
        name = Constants.MOD_ID + "." + name;
        KeyMapping bind = new KeyMapping(name, key, KeyMapping.Category.MISC);
        keyHolder keyHolder = new keyHolder(bind, executor);

        if (FANCY_TOASTS_KEYS.contains(keyHolder)) {
            Debug.warn("Key '{}' already exists", name);
            return;
        }

        FANCY_TOASTS_KEYS.add(keyHolder);
    }

    public static KeyMapping[] getKeys(KeyMapping[] builtinKeys) {
        List<KeyMapping> keys = new ArrayList<>(List.of(builtinKeys));
        for (var keyHolder : FANCY_TOASTS_KEYS) {
            keys.add(keyHolder.key);
        }
        return keys.toArray(new KeyMapping[0]);
    }

    public void tick() {
        for (var keyHolder : FANCY_TOASTS_KEYS) {
            if (keyHolder.key.consumeClick()) {
                keyHolder.executor.execute();
            }
        }
    }
}
