package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.texture.DisplayData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InformationList extends AbstractSelectionList<InformationList.Entry> {
    private final List<InformationListEntry> lines = new ArrayList<>(7);

    public InformationList(Minecraft minecraft, int width, int height, int x, int y, DisplayData displayData, boolean isConfig) {
        super(minecraft, width, height, y, 10);
        this.setX(x);

        this.update(displayData, isConfig);
    }

    public void update(DisplayData displayData, boolean isConfig) {
        if (displayData == null) {
            Debug.error("No Display Data to show in Information List");
            return;
        }

        this.clear();

        this.addLine(displayData.getName(), CommonColors.YELLOW);
        if (isConfig) {
            this.addLine(Component.translatable("fancytoasts.gui.custom"), CommonColors.SOFT_RED);
        }
        this.addSpace();
        this.addLine(Component.translatable("fancytoasts.gui.label.author"), CommonColors.WHITE);
        this.addLine(displayData.getAuthor(), CommonColors.LIGHT_GRAY);
        this.addSpace();
        this.addLine(Component.translatable("fancytoasts.gui.label.description"), CommonColors.WHITE);
        this.addLine(displayData.getDescription(), CommonColors.LIGHT_GRAY);

        this.acceptLines();
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
            this.lines.add(new InformationListEntry(font, textLine, color));
        }
    }

    private void addSpace() {
        this.lines.add(new InformationListEntry(this.minecraft.font, FormattedCharSequence.EMPTY, 0));
    }

    private void acceptLines() {
        for (var line : this.lines) {
            this.addEntry(line);
        }
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
        public void render(@NotNull GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.drawString(this.font, this.content, x, y, this.color);
        }
    }
}
