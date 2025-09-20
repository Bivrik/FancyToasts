package net.bivrik.fancytoasts.client.config;

import net.minecraft.network.chat.Component;

public enum AdvancementToastPosition {
    CENTER("center") {
        @Override
        public int getX(int width, int screenWidth) {
            return screenWidth / 2 - width / 2;
        }
    },
    LEFT("left") {
        @Override
        public int getX(int width, int screenWidth) {
            return 20;
        }
    },
    RIGHT("right") {
        @Override
        public int getX(int width, int screenWidth) {
            return screenWidth - 20 - width;
        }
    };

    private final String name;
    private final Component displayName;

    public abstract int getX(int width, int screenWidth);

    AdvancementToastPosition(String name) {
        this.name = name;
        this.displayName = Component.translatable("fancytoasts.gui.label.position_" + this.name);
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }
}
