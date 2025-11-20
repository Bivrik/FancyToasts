package net.bivrik.fancytoasts.client;

import net.bivrik.fancytoasts.platform.ITickableManager;

import java.util.*;

public class KeyBindingManager implements ITickableManager {
    private final KeyBinding[] keyBindings = KeyBindingRegistry.keyBindings();

    @Override
    public void onTick() {
        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding.key().consumeClick()) {
                keyBinding.keyExecutor().execute();
            }
        }
    }
}
