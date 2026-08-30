package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.client.registry.KeyBindingRegistry;
import net.bivrik.fancytoasts.utility.KeyBinding;

public final class KeyBindingManager {
    private final KeyBinding[] keyBindings;

    public KeyBindingManager() {
        keyBindings = KeyBindingRegistry.getKeyBindings();
    }

    public void tick() {
        for (KeyBinding binding : keyBindings) {
            if (binding.key().consumeClick()) {
                binding.executor().execute();
            }
        }
    }
}
