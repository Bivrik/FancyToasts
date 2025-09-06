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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ResourceLocationList extends ObjectSelectionList<ResourceLocationList.Entry> {
    public abstract static class Entry extends ObjectSelectionList.Entry<Entry> {}

    private final List<ResourceLocation> resourceLocations;
    private Consumer<ResourceLocation> responder;

    public ResourceLocationList(Minecraft minecraft, int width, int height, int x, int y, int itemHeight, List<ResourceLocation> resourceLocations) {
        super(minecraft, width, height, y, itemHeight);
        this.setX(x);

        this.resourceLocations = resourceLocations;

        for (ResourceLocation location : this.resourceLocations) {
            this.addEntry(new ResourceLocationListEntry(this, location));
        }
    }

    public void setResponder(Consumer<ResourceLocation> responder) {
        this.responder = responder;
    }

    @Override
    public int getRowWidth() {
        return this.width - 35;
    }

    public void onFilterUpdate(String filter) {
        this.clearEntries();
        this.refreshScrollAmount();

        for (ResourceLocation location : resourceLocations) {
            if (!location.toLanguageKey().contains(filter)) {
                continue;
            }

            this.addEntry(new ResourceLocationListEntry(this, location));
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (CommonInputs.selected(keyCode)) {
            var selectedEntry = (ResourceLocationListEntry) this.getSelected();
            assert selectedEntry != null;
            selectedEntry.select();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public static final class ResourceLocationListEntry extends Entry {
        private final ResourceLocation location;
        private final Minecraft minecraft;
        private final Font font;
        private final ResourceLocationList list;

        private long lastClickTime;

        public ResourceLocationListEntry(ResourceLocationList list, ResourceLocation location) {
            this.location = location;
            this.minecraft = list.minecraft;
            this.font = list.minecraft.font;
            this.list = list;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.empty();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.list.responder.accept(location);

            if (Util.getMillis() - lastClickTime >= 250L) {
                lastClickTime = Util.getMillis();
            }
            else {
                select();
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        public void select() {
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            String name = location.toLanguageKey().replace('/', '.');

            var nameList = font.split(Component.translatable(name), width - 6);

            if (nameList.size() == 1) {
                guiGraphics.drawString(font, nameList.getFirst(), x + 3, y + 3, CommonColors.WHITE);
            }
            else {
                guiGraphics.drawString(font, nameList.get(0), x + 3, y - 1, CommonColors.WHITE);
                guiGraphics.drawString(font, nameList.get(1), x + 3, y + 7, CommonColors.WHITE);
            }

        }
    }
}
