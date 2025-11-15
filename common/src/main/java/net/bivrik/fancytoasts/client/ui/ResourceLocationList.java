package net.bivrik.fancytoasts.client.ui;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ResourceLocationList extends ObjectSelectionList<ResourceLocationList.Entry> {
    private ResourceLocation[] resourceLocations;
    private SettingType settingType;
    private Consumer<ResourceLocation> acceptResponder;
    private Consumer<ResourceLocation> selectResponder;

    public ResourceLocationList(Minecraft minecraft, int width, int height, int x, int y, int itemHeight, SettingType settingType) {
        super(minecraft, width, height, y, itemHeight);
        this.setX(x);

        setResourceLocations(settingType);
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
        return this.getX() + this.width / 2 - this.getRowWidth() / 2 - 3;
    }

    @Override
    protected int scrollBarX() {
        return this.getX() + this.width - 8;
    }

    public void onSearchUpdate(String search) {
        clear();
        search = search.toLowerCase(Locale.ROOT);

        for (ResourceLocation location : resourceLocations) {
            if (!settingType.getDisplayData(location).getDisplayName().getString().toLowerCase(Locale.ROOT).contains(search)) {
                continue;
            }

            this.addEntry(new ResourceLocationListEntry(this, location, settingType.getDisplayData(location).getDisplayName()));
        }
    }

    public void onFilterUpdate(ResourceLocationFilter filter) {
        switch (filter) {
            case A_Z -> sortAZ();
            case Z_A -> Arrays.sort(resourceLocations, (loc1, loc2) -> {
                String name1 = settingType.getDisplayData(loc1).getDisplayName().getString();
                String name2 = settingType.getDisplayData(loc2).getDisplayName().getString();
                return name2.compareTo(name1);
            });
            case BUILT_IN -> resourceLocations = typeSort(resourceLocations, true);
            case CUSTOM -> resourceLocations = typeSort(resourceLocations, false);
        }

        refillList();
    }

    private void sortAZ() {
        Arrays.sort(resourceLocations, (loc1, loc2) -> {
            String name1 = settingType.getDisplayData(loc1).getDisplayName().getString();
            String name2 = settingType.getDisplayData(loc2).getDisplayName().getString();
            return name1.compareTo(name2);
        });
    }

    private ResourceLocation[] typeSort(ResourceLocation[] locations, boolean isBuiltInSortType) {
        sortAZ();

        List<ResourceLocation> sorted = new ArrayList<>(locations.length / 2);
        List<ResourceLocation> leftovers = new ArrayList<>(); // count can be 0

        for (var location : locations) {
            boolean isBuiltIn = location.getNamespace().equals("fancytoasts") || location.getNamespace().equals("minecraft");
            boolean isConfig = location.toLanguageKey().contains(Constants.MOD_CONFIG);
            boolean isCustom = !isBuiltIn || isConfig;

            if (isBuiltInSortType != isCustom) {
                sorted.add(location);
            }
            else {
                leftovers.add(location);
            }
        }
        sorted.addAll(leftovers);

        return sorted.toArray(new ResourceLocation[0]);
    }

    public void setResourceLocations(SettingType settingType) {
        this.settingType = settingType;
        this.resourceLocations = this.settingType.getKeySet();
        Arrays.sort(this.resourceLocations);

        refillList();
    }

    private void refillList() {
        clear();

        for (ResourceLocation location : resourceLocations) {
            this.addEntry(new ResourceLocationListEntry(this, location, settingType.getDisplayData(location).getDisplayName()));
        }
    }

    private void clear() {
        this.clearEntries();
        this.refreshScrollAmount();
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
        private List<FormattedCharSequence> nameList;
        private final boolean isConfig;

        private long lastClickTime;

        public ResourceLocationListEntry(ResourceLocationList list, ResourceLocation location, Component name) {
            this.list = list;
            this.location = location;
            this.minecraft = this.list.minecraft;
            this.font = this.minecraft.font;
            this.isConfig = location.toLanguageKey().contains(Constants.MOD_CONFIG);

            if (name != null) {
                this.nameList = this.font.split(name, this.list.getRowWidth() - 8 - 2);
            }
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent e, boolean isDoubleClick) {
            if (Util.getMillis() - lastClickTime >= 250L) {
                lastClickTime = Util.getMillis();
                select();
            }
            else {
                accept();
            }

            return super.mouseClicked(e, isDoubleClick);
        }

        @Override
        public boolean keyPressed(KeyEvent e) {
            if (e.isSelection()) {
                accept();
            }

            return super.keyPressed(e);
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
        public int getX() {
            return super.getX() - 3;
        }

        @Override
        public int getWidth() {
            return super.getWidth() + 3;
        }

        @Override
        public void renderContent(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            int x = this.getX();
            int y = this.getY();

            int mainColor;
            int secondColor;

            if (isFocused()) {
                mainColor = Colors.YELLOW;
                secondColor = Colors.PURPLE;
            }
            else {
                if (isHovering) {
                    guiGraphics.fill(x, y, x + getWidth(), y + getHeight(), Colors.alpha(32, Colors.WHITE));
                    guiGraphics.fill(x + 1, y + 1, x + getWidth() - 1, y + getHeight() - 1, Colors.alpha(128, Colors.BLACK));
                }

                mainColor = Colors.WHITE;
                secondColor = Colors.LIGHT_GRAY;
            }

            if (this.nameList.size() == 1) {
                guiGraphics.drawString(this.font, this.nameList.getFirst(), x + 3, y + 5, mainColor);
            }
            else {
                guiGraphics.drawString(this.font, this.nameList.get(1), x + 3, y + 8, secondColor);
                guiGraphics.drawString(this.font, this.nameList.get(0), x + 3, y + 2, mainColor);
            }

            if (this.isConfig) {
                guiGraphics.drawString(this.font, Component.literal("c"), this.getX() + this.getWidth() - 10, y + 5, Colors.LIGHT_GRAY);
            }
        }
    }
}
