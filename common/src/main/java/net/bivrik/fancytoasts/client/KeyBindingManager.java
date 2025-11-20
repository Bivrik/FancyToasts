package net.bivrik.fancytoasts.client;

import net.bivrik.fancytoasts.platform.ITickableManager;

import java.util.*;

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
