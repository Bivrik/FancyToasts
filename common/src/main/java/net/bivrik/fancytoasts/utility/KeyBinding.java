package net.bivrik.fancytoasts.utility;

import net.minecraft.client.KeyMapping;

public record KeyBinding(KeyMapping key, keyExecutor executor) {
    @FunctionalInterface
    public interface keyExecutor {
        void execute();
    }
}
