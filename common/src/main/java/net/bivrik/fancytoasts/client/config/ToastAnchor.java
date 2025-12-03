package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.network.chat.Component;
import org.joml.Vector2d;

public enum ToastAnchor {
    TOP_LEFT("top_left", 0.0f, 0.0f),
    TOP("top", 0.5f, 0.0f),
    TOP_RIGHT("top_right", 1.0f, 0.0f),

    CENTER_LEFT("center_left", 0.0f, 0.5f),
    CENTER("center", 0.5f, 0.5f),
    CENTER_RIGHT("center_right", 1.0f, 0.5f),

    BOTTOM_LEFT("bottom_left", 0.0f, 1.0f),
    BOTTOM("bottom", 0.5f, 1.0f),
    BOTTOM_RIGHT("bottom_right", 1.0f, 1.0f);

    private final float anchorX;
    private final float anchorY;
    private final String name;
    private final Component displayName;

    ToastAnchor(String name, float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.name = name;
        this.displayName = Components.of("anchor." + this.name);
    }

    public Vector2d getPosition(int width, int height, int offsetX, int offsetY) {
        int anchoredWidth = (int) (width * anchorX);
        int anchoredHeight = (int) (height * anchorY);

        return new Vector2d(anchoredWidth + offsetX, anchoredHeight + offsetY);
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }
}
