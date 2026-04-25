package net.bivrik.fancytoasts.platform.services;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.GuiGraphics;

public interface IDawnEraHelper {
    default void drawCustomIcon(GuiGraphics guiGraphics, Advancement advancement, int x, int y) {}

    default boolean isCustomIcon(Advancement advancement) {
        return false;
    }

    default boolean isLoaded() {
        return Services.PLATFORM.isModLoaded(Constants.Compatibilities.DAWN_ERA_UD);
    }
}
