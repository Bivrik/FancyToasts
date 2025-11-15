package net.bivrik.fancytoasts.client;

import net.minecraft.client.KeyMapping;

public record KeyBinding(KeyMapping key, keyExecutor keyExecutor) {
    public interface keyExecutor {
        void execute();
    }
}
