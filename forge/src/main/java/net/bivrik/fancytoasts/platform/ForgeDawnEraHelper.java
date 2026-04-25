package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.DawnEraCompat;
import net.bivrik.fancytoasts.platform.services.IDawnEraHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.GuiGraphics;

public class ForgeDawnEraHelper implements IDawnEraHelper {
    @Override
    public void drawCustomIcon(GuiGraphics guiGraphics, Advancement advancement, int x, int y) {
        if (isLoaded()) {
            DawnEraCompat.drawCustomIcon(guiGraphics, advancement, x, y);
        }
    }

    @Override
    public boolean isCustomIcon(Advancement advancement) {
        if (isLoaded()) {
            return DawnEraCompat.isCustomIcon(advancement);
        }

        return IDawnEraHelper.super.isCustomIcon(advancement);
    }
}
