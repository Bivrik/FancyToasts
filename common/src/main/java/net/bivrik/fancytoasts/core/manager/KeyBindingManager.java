package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.client.registry.KeyBindingRegistry;
import net.bivrik.fancytoasts.utility.KeyBinding;

public class KeyBindingManager {
    private KeyBinding[] keyBindings;

    public KeyBindingManager() {
        updateKeyBindings();
    }

    public void updateKeyBindings() {
        keyBindings = KeyBindingRegistry.keyBindings();
    }

    public void tick() {
        for (KeyBinding binding : keyBindings) {
            if (binding.key().consumeClick()) {
                binding.keyExecutor().execute();
            }
        }
    }
}
