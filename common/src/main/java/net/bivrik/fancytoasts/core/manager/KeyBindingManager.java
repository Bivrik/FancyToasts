package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.client.registry.KeyBindingRegistry;
import net.bivrik.fancytoasts.core.ITickableManager;
import net.bivrik.fancytoasts.utility.KeyBinding;

public class KeyBindingManager implements ITickableManager {
    private KeyBinding[] keyBindings;

    public void updateKeyBindings() {
        keyBindings = KeyBindingRegistry.keyBindings();
    }

    @Override
    public void onModInit() {
        updateKeyBindings();
    }

    @Override
    public void onTick() {
        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding.key().consumeClick()) {
                keyBinding.keyExecutor().execute();
            }
        }
    }
}
