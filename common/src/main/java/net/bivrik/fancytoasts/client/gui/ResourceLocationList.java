package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Debug;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class ResourceLocationList extends ObjectSelectionList<ResourceLocationList.Entry> {
    private ResourceLocation[] resourceLocations;
    private Consumer<ResourceLocation> acceptResponder;
    private Consumer<ResourceLocation> selectResponder;

    public ResourceLocationList(Minecraft minecraft, int width, int height, int x, int y, int itemHeight, ResourceLocation[] resourceLocations) {
        super(minecraft, width, height, y, itemHeight);
        this.setX(x);

        this.setResourceLocations(resourceLocations);
    }

    public void setAcceptResponder(Consumer<ResourceLocation> acceptResponder) {
        this.acceptResponder = acceptResponder;
    }

    public void setSelectResponder(Consumer<ResourceLocation> selectResponder) {
        this.selectResponder = selectResponder;
    }

    @Override
    public int getRowWidth() {
        return this.width - 6 - 16;
    }

    @Override
    public int getRowLeft() {
        return this.getX() + this.width / 2 - this.getRowWidth() / 2 + 4;
    }

    @Override
    protected int scrollBarX() {
        return this.getX() + this.width - 8;
    }

    public void onFilterUpdate(String filter) {
        this.clear();
        filter = filter.toLowerCase(Locale.ROOT);

        for (ResourceLocation location : this.resourceLocations) {
            if (!location.toLanguageKey().toLowerCase(Locale.ROOT).contains(filter)) {
                continue;
            }

            this.addEntry(new ResourceLocationListEntry(this, location));
        }
    }

    public void setResourceLocations(ResourceLocation[] resourceLocations) {
        this.resourceLocations = resourceLocations;
        this.refillList();
    }

    private void refillList() {
        this.clear();

        for (ResourceLocation location : this.resourceLocations) {
            this.addEntry(new ResourceLocationListEntry(this, location));
        }
    }

    private void clear() {
        this.clearEntries();
        this.refreshScrollAmount();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (CommonInputs.selected(keyCode)) {
            var selectedEntry = this.getSelected();
            if (selectedEntry instanceof ResourceLocationListEntry) {
                ((ResourceLocationListEntry) selectedEntry).accept();
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected abstract static class Entry extends ObjectSelectionList.Entry<Entry> {
        @Override
        public @NotNull Component getNarration() {
            return Component.empty();
        }
    }

    private static final class ResourceLocationListEntry extends Entry {
        private final ResourceLocation location;
        private final Minecraft minecraft;
        private final Font font;
        private final ResourceLocationList list;
        private final List<FormattedCharSequence> nameList;

        private long lastClickTime;

        public ResourceLocationListEntry(ResourceLocationList list, ResourceLocation location) {
            this.list = list;
            this.location = location;
            this.minecraft = this.list.minecraft;
            this.font = this.minecraft.font;

            String name = this.location.toLanguageKey().replace('/', '.').replace(".png", "").replace("s.gui.advancement_toasts","");
            this.nameList = this.font.split(Component.translatable(name), this.list.getRowWidth() - 8 - 2);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (Util.getMillis() - lastClickTime >= 250L) {
                lastClickTime = Util.getMillis();
                select();
            }
            else {
                accept();
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        private void select() {
            if (this.list.selectResponder != null) {
                this.list.selectResponder.accept(this.location);
            }
            else {
                Debug.error("There is no select responder for Resource Location List. Could not select location: " + this.location);
            }
        }

        private void accept() {
            if (this.list.acceptResponder != null) {
                this.list.acceptResponder.accept(this.location);
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            else {
                Debug.error("There is no accept responder for Resource Location List. Could not accept location: " + this.location);
            }
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            if (this.nameList.size() == 1) {
                guiGraphics.drawString(this.font, this.nameList.getFirst(), x, y + 3, CommonColors.WHITE);
            }
            else {
                guiGraphics.drawString(this.font, this.nameList.get(1), x, y + 6, CommonColors.LIGHT_GRAY);
                guiGraphics.drawString(this.font, this.nameList.get(0), x, y, CommonColors.WHITE);
            }

        }
    }
}
