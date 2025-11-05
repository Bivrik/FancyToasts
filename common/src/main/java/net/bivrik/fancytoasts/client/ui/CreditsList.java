package net.bivrik.fancytoasts.client.ui;

import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class CreditsList extends AbstractSelectionList<CreditsList.Entry> {
    private final List<CreditsListEntry> lines = new ArrayList<>(10);
    private float scrollSpeed = 0.4f;

    public CreditsList(Minecraft minecraft, int width, int height, int x, int y, CreditsManager.CreditsData data) {
        super(minecraft, width, height, y, 18);
        this.setX(x);

        updateList(data);
    }

    private void updateList(CreditsManager.CreditsData data) {
        for (int i = 0; i < this.height / defaultEntryHeight + 2; i++) {
            addSpace();
        }

        for (String category : data.categories().keySet()) {
            addCategory(category);
            for (CreditsManager.CreditsData.User user : data.categories().get(category)) {
                addLine(user);
            }
            addSpace();
        }

        for (int i = 0; i < this.height / defaultEntryHeight; i++) {
            addSpace();
        }

        acceptLines();
    }

    @Override
    public void refreshScrollAmount() {}

    private void acceptLines() {
        for (var line : this.lines) {
            this.addEntry(line);
        }
    }

    private void addSpace() {
        lines.add(new CreditsListEntry(this, "", null, false));
    }

    private void addCategory(String category) {
        lines.add(new CreditsListEntry(this, category, null, true));
    }

    private void addLine(CreditsManager.CreditsData.User user) {
        lines.add(new CreditsListEntry(this, user.name(), user.annotation(), false));
    }

    public void scroll() {
        this.setScrollAmount(scrollAmount() + scrollSpeed);

        if (scrollAmount() == maxScrollAmount()) {
            setScrollAmount(0);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_SPACE) {
            scrollSpeed = 1.2f;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_SPACE) {
            scrollSpeed = 0.4f;
        }

        return super.keyReleased(event);
    }

    @Override
    protected void renderListBackground(@NotNull GuiGraphics guiGraphics) {}

    @Override
    protected void renderListSeparators(@NotNull GuiGraphics guiGraphics) {}

    @Override
    protected boolean scrollbarVisible() {
        return false;
    }

    @Override
    protected double scrollRate() {
        return 0;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}

    protected abstract static class Entry extends AbstractSelectionList.Entry<Entry> {}

    private static class CreditsListEntry extends Entry {
        private final CreditsList list;
        private final String content;
        private final String annotation;
        private final Font font;
        private final int color;
        private final boolean isCategory;
        private final boolean isSpace;

        public CreditsListEntry(CreditsList list, String content, String annotation, boolean isCategory) {
            this.list = list;
            this.content = content.compareTo("{user.name}") == 0 ? this.list.minecraft.getUser().getName() : content;
            this.annotation = annotation;
            this.font = this.list.minecraft.font;
            this.color = !isCategory ? Colors.WHITE : Colors.YELLOW;
            this.isCategory = isCategory;
            this.isSpace = content.isEmpty();
        }

        @Override
        public void renderContent(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            if (isSpace) {
                return;
            }

            if (isCategory) {
                guiGraphics.drawCenteredString(font, Components.of("gui.label." + content), list.getWidth() / 2, this.getY(), color);
            }
            else {
                guiGraphics.drawString(font, content, this.getX(), this.getY(), color);
                if (annotation != null && !annotation.isEmpty()) {
                    guiGraphics.drawString(font, annotation, this.getX() + font.width(content) + 8, this.getY(), Colors.LIGHT_GRAY);
                }
            }
        }
    }
}
