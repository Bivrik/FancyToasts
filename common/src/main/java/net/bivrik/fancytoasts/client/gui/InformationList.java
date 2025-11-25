package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InformationList extends AbstractSelectionList<InformationList.Entry> {
    private final List<InformationListEntry> lines = new ArrayList<>(7);
    private ResourceLocation location;

    public InformationList(Minecraft minecraft, int width, int height, int x, int y, DisplayData displayData, boolean isConfig) {
        super(minecraft, width, height, y, 10);
        this.setX(x);

        this.update(displayData, isConfig, true);
    }

    public void update(DisplayData displayData, boolean isConfig, boolean isSelected) {
        if (displayData == null) {
            Debug.error("No Display Data to show in Information List");
            return;
        }

        location = isSelected ? ResourceLocations.of("icons/success") : ResourceLocations.of("icons/looking");

        clear();

        this.addLine(displayData.getDisplayName(), Colors.YELLOW);
        if (isConfig) {
            addLine(Component.translatable("fancytoasts.gui.custom"), Colors.RED);
        }
        addSpace();
        addLine(Component.translatable("fancytoasts.gui.label.author"), Colors.WHITE);
        addLine(displayData.getAuthor(), Colors.LIGHT_GRAY);
        addSpace();
        addLine(Component.translatable("fancytoasts.gui.label.description"), Colors.WHITE);
        addLine(displayData.getDisplayDescription(), Colors.LIGHT_GRAY);

        acceptLines();
    }

    private void clear() {
        this.clearEntries();
        this.refreshScrollAmount();
        lines.clear();
    }

    private void addLine(Component content, int color) {
        Font font = this.minecraft.font;

        List<FormattedCharSequence> textLines = font.split(content, this.getRowWidth());
        for (var textLine : textLines) {
            lines.add(new InformationListEntry(font, textLine, color));
        }
    }

    private void addSpace() {
        lines.add(new InformationListEntry(this.minecraft.font, FormattedCharSequence.EMPTY, 0));
    }

    private void acceptLines() {
        for (var line : lines) {
            this.addEntry(line);
        }
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        GuiContext context = new GuiContext(guiGraphics);
        context.drawSprite(location, this.getRight() - 8 - 3, this.getY() + 1, 8, 8);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}

    @Override
    public int getRowWidth() {
        return this.width - 16 - 8;
    }

    @Override
    public int getRowLeft() {
        return this.getX() + 8;
    }

    @Override
    protected int scrollBarX() {
        return this.getX() + this.width - 8;
    }

    protected abstract static class Entry extends AbstractSelectionList.Entry<Entry> {}

    private static final class InformationListEntry extends Entry {
        private final Font font;
        private final FormattedCharSequence content;
        private final int color;

        public InformationListEntry(Font font, FormattedCharSequence content, int color) {
            this.font = font;
            this.content = content;
            this.color = color;
        }

        @Override
        public void renderContent(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.drawString(font, content, this.getX(), this.getY() + 3, color);
        }
    }
}
