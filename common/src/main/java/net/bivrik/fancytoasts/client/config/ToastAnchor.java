package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.network.chat.Component;
import org.joml.Vector2d;

public enum ToastAnchor {
    TOP_LEFT("top_left", 0.0f, 0.0f, 100, -50),
    TOP("top", 0.5f, 0.0f, 0, -50),
    TOP_RIGHT("top_right", 1.0f, 0.0f, -100, -50),

    CENTER_LEFT("center_left", 0.0f, 0.5f, 100, 0),
    CENTER("center", 0.5f, 0.5f, 0, 0),
    CENTER_RIGHT("center_right", 1.0f, 0.5f, -100, 0),

    BOTTOM_LEFT("bottom_left", 0.0f, 1.0f, 100, 40),
    BOTTOM("bottom", 0.5f, 1.0f, 0, 40),
    BOTTOM_RIGHT("bottom_right", 1.0f, 1.0f, -100, 40);

    private final float anchorX;
    private final float anchorY;
    private final int offsetX;
    private final int offsetY;
    private final String name;
    private final Component displayName;

    ToastAnchor(String name, float anchorX, float anchorY, int offsetX, int offsetY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.name = name;
        this.displayName = Components.of("anchor." + this.name);
    }

    public int getBaseOffsetX() {
        return offsetX;
    }

    public int getBaseOffsetY() {
        return offsetY;
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
