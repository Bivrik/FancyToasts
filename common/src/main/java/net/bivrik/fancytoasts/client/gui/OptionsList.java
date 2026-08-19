package net.bivrik.fancytoasts.client.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OptionsList extends ContainerObjectSelectionList<OptionsList.Entry> {
    private static final int MAX_ROW_WEIGHT = 4;

    private final HashMap<AbstractWidget, WidgetWidthType> widgets = new LinkedHashMap<>();
    private final Screen screen;

    public OptionsList(Minecraft minecraft, int width, int height, int y, int itemHeight, Screen screen) {
        super(minecraft, width, height, y, itemHeight);
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    @Override
    public int getRowWidth() {
        return 310;
    }

    public <T extends AbstractWidget> T addElement(T widget, WidgetWidthType widthType) {
        widget.setWidth(widthType.getWidth());
        widgets.put(widget, widthType);
        return widget;
    }

    public <T extends AbstractWidget> T addElement(T widget) {
        return addElement(widget, WidgetWidthType.MEDIUM);
    }
    
    public void alignElements() {
        List<EntryHolder> holderList = new ArrayList<>();
        holderList.add(new EntryHolder());

        super.clearEntries();
        for (var widgetEntry : widgets.entrySet()) {
            AbstractWidget widget = widgetEntry.getKey();
            WidgetWidthType widthType = widgetEntry.getValue();

            boolean added = false;
            for (EntryHolder h : holderList) {
                if (h.getWeight() + widthType.getWeight() <= MAX_ROW_WEIGHT) {
                    added = true;
                    h.addChild(widget, widthType);
                    break;
                }
            }
            if (!added) {
                EntryHolder h = new EntryHolder();
                h.addChild(widget, widthType);
                holderList.add(h);
            }
        }

        for (EntryHolder h : holderList) {
            super.addEntry(new Entry(this, h.getChildren()));
        }
    }

    private static class EntryHolder {
        private final List<AbstractWidget> children = new ArrayList<>();
        private int weight = 0;

        public void addChild(AbstractWidget widget, WidgetWidthType type) {
            children.add(widget);
            weight += type.getWeight();
        }

        public List<AbstractWidget> getChildren() {
            return ImmutableList.copyOf(children);
        }

        public int getWeight() {
            return weight;
        }
    }

    protected static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private final OptionsList list;
        private final List<AbstractWidget> children;

        protected Entry(OptionsList list, List<AbstractWidget> widgets) {
            this.list = list;
            this.children = ImmutableList.copyOf(widgets);
        }

        @Override
        public void extractContent(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            int i = 0;
            for (var widget : children) {
                widget.setPosition(list.getRowLeft() + i, this.getContentY());
                widget.extractRenderState(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
                i += widget.getWidth() + 10;
            }
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return children;
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return children;
        }
    }
}
