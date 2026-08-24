package net.bivrik.fancytoasts.client.credits;

import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class CreditsList extends AbstractSelectionList<CreditsList.Entry> {
    private final List<Entry> lines = new ArrayList<>(10);
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

        for (String category : data.getCategories().keySet()) {
            if (!data.getCategories().get(category).isEmpty()) {
                addCategory(category);
                for (CreditsManager.CreditsData.User user : data.getCategories().get(category)) {
                    addLine(user);
                }
                addSpace();
            }
        }

        for (int i = 0; i < this.height / defaultEntryHeight; i++) {
            addSpace();
        }

        acceptLines();
    }

    private void acceptLines() {
        for (var line : lines) {
            this.addEntry(line);
        }
    }

    private void addSpace() {
        lines.add(new SpaceEntry(this, ""));
    }

    private void addCategory(String category) {
        lines.add(new CategoryEntry(this, category));
    }

    private void addLine(CreditsManager.CreditsData.User user) {
        String content = user.name().compareTo("{user.name}") != 0 ? user.name() : this.minecraft.getUser().getName();
        lines.add(new UserEntry(this, content, user.annotation()));
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

    // Don't move bro, stop it
    @Override
    public void refreshScrollAmount() {}

    @Override
    protected void extractListBackground(@NotNull GuiGraphicsExtractor graphics) {}

    @Override
    protected void extractListSeparators(@NotNull GuiGraphicsExtractor graphics) {}

    @Override
    protected boolean scrollable() {
        return false;
    }

    @Override
    protected int scrollBarX() {
        return this.width + this.width;
    }

    @Override
    protected double scrollRate() {
        return 0;
    }
    //

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}

    protected abstract static class Entry extends AbstractSelectionList.Entry<Entry> {
        protected final CreditsList parentList;
        protected final String content;
        protected final Font font;

        public Entry(CreditsList parentList, String content) {
            this.parentList = parentList;
            this.content = content;
            this.font = this.parentList.minecraft.font;
        }

        @Override
        public void extractContent(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {}
    }

    private static class CategoryEntry extends Entry {
        private final Component displayName;
        private final int xCenter;

        public CategoryEntry(CreditsList parentList, String content) {
            super(parentList, content);

            this.displayName = Component.translatable(Constants.MOD_ID + ".label." + this.content);
            this.xCenter = parentList.getWidth() / 2;
        }

        @Override
        public void extractContent(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            graphics.centeredText(font, displayName, xCenter, getY(), Color.YELLOW.getARGB());
        }
    }

    private static class SpaceEntry extends Entry {
        public SpaceEntry(CreditsList parentList, String content) {
            super(parentList, content);
        }
    }

    private static class UserEntry extends Entry {
        private final String annotation;
        private final boolean isValidAnnotation;

        public UserEntry(CreditsList parentList, String content, String annotation) {
            super(parentList, content);
            this.annotation = annotation;
            this.isValidAnnotation = this.annotation != null && !this.annotation.isEmpty();
        }

        @Override
        public void extractContent(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            graphics.text(this.font, this.content, this.getX(), this.getY(), Color.WHITE.getARGB());

            if (isValidAnnotation) {
                graphics.text(this.font, annotation, this.getX() + font.width(this.content) + 8, this.getY(), Color.LIGHT_GRAY.getARGB());
            }
        }
    }
}
