package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.network.chat.Component;
import org.joml.Vector2d;

import java.util.Locale;

public enum ToastAnchor {
    TOP_LEFT(0.0f, 0.0f),
    TOP(0.5f, 0.0f),
    TOP_RIGHT(1.0f, 0.0f),

    CENTER_LEFT(0.0f, 0.5f),
    CENTER(0.5f, 0.5f),
    CENTER_RIGHT(1.0f, 0.5f),

    BOTTOM_LEFT(0.0f, 1.0f),
    BOTTOM(0.5f, 1.0f),
    BOTTOM_RIGHT(1.0f, 1.0f);

    private final float anchorX;
    private final float anchorY;

    ToastAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public Vector2d getPosition(int width, int height, int offsetX, int offsetY) {
        int anchoredWidth = (int) (width * anchorX);
        int anchoredHeight = (int) (height * anchorY);

        return new Vector2d(anchoredWidth + offsetX, anchoredHeight + offsetY);
    }

    public Component getName() {
        return Components.of("gui.anchor." + this.name().toLowerCase(Locale.ROOT));
    }
}
