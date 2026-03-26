package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.client.gui.screen.GeneralConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingsList extends ContainerObjectSelectionList<SettingsList.Entry> {
    private final Screen screen;

    public SettingsList(Minecraft minecraft, int width, int height, int y, int itemHeight, Screen screen) {
        super(minecraft, width, height, y, itemHeight);

        this.screen = screen;
    }

    public <T extends GuiEventListener & Renderable & NarratableEntry> T addEntry(AbstractWidget widget) {
        //super.addEntry(new Entry(widget));
        @SuppressWarnings("unchecked")
        T result = (T) widget;
        return result;
    }

    protected static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private final AbstractWidget widget;
        private final Screen screen;

        protected Entry(AbstractWidget widget, Screen screen) {
            this.widget = widget;
            this.screen = screen;
        }

        @Override
        public void renderContent(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            int i = 0;
            int j = this.screen.width / 2 - 155;

            /*for (AbstractWidget abstractWidget : this.children) {
                abstractWidget.setPosition(j + i, this.getContentY());
                abstractWidget.render(guiGraphics, mouseX, mouseY, partialTick);
                i += 160;
            }*/

            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }
    }
}
