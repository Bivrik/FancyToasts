package net.bivrik.fancytoasts.compat;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import ru.xishnikus.thedawnera.client.event.AdvancementEventListener;
import ru.xishnikus.thedawnera.client.event.AdvancementRenderEvent;
import ru.xishnikus.thedawnera.common.advancement.TDEAdvancements;

import java.util.List;

public class DawnEraCompat {
    private static final List<ResourceLocation> CUSTOM_ICONS = List.of(TDEAdvancements.THE_DAWN_ERA, TDEAdvancements.LONG_LIVE_THE_KING,
            TDEAdvancements.NO_KING_RULES_FOREVER, TDEAdvancements.LIVING_FOSSIL, TDEAdvancements.NAIVETY_ITSELF);

    public static void drawCustomIcon(GuiGraphics guiGraphics, Advancement advancement, int x, int y) {
        AdvancementEventListener.onRenderAdvancementIcon(new AdvancementRenderEvent.ElementIcon(guiGraphics, advancement, 0, 0, x, y));
    }

    public static boolean isCustomIcon(Advancement advancement) {
        ResourceLocation id = advancement.getId();
        return CUSTOM_ICONS.contains(id);
    }
}
