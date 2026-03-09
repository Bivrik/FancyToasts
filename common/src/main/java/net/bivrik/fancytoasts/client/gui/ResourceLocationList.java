package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ResourceLocationList extends ObjectSelectionList<ResourceLocationList.Entry> {
    private final Map<ResourceLocation, String> displayNames = new HashMap<>();
    private ResourceLocation[] resourceLocations;
    private SettingType settingType;

    private Consumer<ResourceLocation> selectResponder;
    private Consumer<ResourceLocation> focusResponder;

    private ResourceLocationFilter filter = ResourceLocationFilter.A_Z;
    private String search = "";

    public ResourceLocationList(Minecraft minecraft, int width, int height, int x, int y, int itemHeight, SettingType settingType) {
        super(minecraft, width, height, y, itemHeight);
        this.setX(x);

        setResourceLocations(settingType);
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

    public void setResourceLocations(SettingType settingType) {
        this.settingType = settingType;
        this.resourceLocations = this.settingType.getKeySet();
        this.displayNames.clear();
        for (ResourceLocation location : this.resourceLocations) {
            this.displayNames.computeIfAbsent(location, location1 -> this.settingType.getDisplayData(location1).getDisplayName().getString().toLowerCase(Locale.ROOT));
        }
        sortAZ();

        refillList();
    }

    private void refillList() {
        clearList();

        if (search.isEmpty()) {
            for (ResourceLocation location : resourceLocations) {
                this.addEntry(new ResourceLocationListEntry(this, location, settingType.getDisplayData(location).getDisplayName()));
            }
        } else {
            for (ResourceLocation location : resourceLocations) {
                if (displayNames.get(location).contains(search)) {
                    this.addEntry(new ResourceLocationListEntry(this, location, settingType.getDisplayData(location).getDisplayName()));
                }
            }
        }
    }

    private void clearList() {
        this.clearEntries();
        this.refreshScrollAmount();
    }

    public void setSearch(String search) {
        if (search == null) search = "";

        String lowercaseSearch = search.toLowerCase(Locale.ROOT);
        if (this.search.equals(lowercaseSearch)) {
            return;
        }

        this.search = lowercaseSearch;
        refillList();
    }

    public void setFilter(ResourceLocationFilter filter) {
        if (this.filter == filter || filter == null) {
            return;
        }

        this.filter = filter;
        onFilterUpdate();
    }

    private void onFilterUpdate() {
        switch (filter) {
            case A_Z -> sortAZ();
            case Z_A -> sortAZ(Comparator.reverseOrder());
            case BUILT_IN -> typeSort(true);
            case CUSTOM -> typeSort(false);
        }

        refillList();
    }

    private void sortAZ(Comparator<String> comparator) {
        Arrays.sort(resourceLocations, (loc1, loc2) -> comparator.compare(displayNames.get(loc1), displayNames.get(loc2)));
    }
    private void sortAZ() {
        sortAZ(Comparator.naturalOrder());
    }

    private void typeSort(boolean isBuiltInFilterType) {
        sortAZ(); // Sort A-Z first, so the names inside categories
        // would be sorted by alphabetical order too! Example:
        // built-ins: A D F, configs: B C E, and it will be: A D F B C E

        List<ResourceLocation> builtInLocations = new ArrayList<>(resourceLocations.length * 2 / 3);
        List<ResourceLocation> configLocations = new ArrayList<>(); // can be 0

        for (var location : resourceLocations) {
            String namespace = location.getNamespace();
            boolean isBuiltIn = namespace.equals(Constants.MOD_ID) || namespace.equals(Constants.Compatibilities.MINECRAFT_ID);
            boolean isConfig = location.getPath().contains(Constants.CONFIG) || !isBuiltIn;

            if (isBuiltInFilterType == !isConfig) {
                builtInLocations.add(location);
            } else {
                configLocations.add(location);
            }
        }
        builtInLocations.addAll(configLocations);

        resourceLocations = builtInLocations.toArray(new ResourceLocation[0]);
    }

    public ResourceLocationList setSelectResponder(Consumer<ResourceLocation> selectResponder) {
        this.selectResponder = selectResponder;
        return this;
    }

    public ResourceLocationList setFocusResponder(Consumer<ResourceLocation> focusResponder) {
        this.focusResponder = focusResponder;
        return this;
    }

    protected abstract static class Entry extends ObjectSelectionList.Entry<Entry> {
        @Override
        public @NotNull Component getNarration() {
            return Component.empty();
        }
    }

    private static final class ResourceLocationListEntry extends Entry {
        private final ResourceLocationList parentList;
        private final ResourceLocation id;
        private final boolean isConfig;
        private final Font font;
        private final SoundManager soundManager;

        private List<FormattedCharSequence> nameLines = new ArrayList<>();
        private long lastClickTime;

        public ResourceLocationListEntry(ResourceLocationList parentList, ResourceLocation id, Component name) {
            this.parentList = parentList;
            Minecraft minecraft = this.parentList.minecraft;
            this.soundManager = minecraft.getSoundManager();
            this.font = minecraft.font;
            this.id = id;
            this.isConfig = this.id.getPath().contains(Constants.CONFIG);
            if (name != null) this.nameLines = this.font.split(name, this.parentList.getRowWidth() - 8 - 2);
        }

        @Override
        public boolean mouseClicked(@NotNull MouseButtonEvent e, boolean isDoubleClick) {
            if (Util.getMillis() - lastClickTime >= 250L) {
                lastClickTime = Util.getMillis();
                focus();
            }
            else {
                select();
            }

            return super.mouseClicked(e, isDoubleClick);
        }

        @Override
        public boolean keyPressed(KeyEvent e) {
            if (e.isSelection()) {
                select();
            }

            return super.keyPressed(e);
        }

        private void focus() {
            if (parentList.focusResponder != null) {
                parentList.focusResponder.accept(this.id);
            }
            else {
                Debug.error("There is no select responder for Resource Location List. Could not select location: " + id);
            }
        }

        private void select() {
            if (parentList.selectResponder != null) {
                parentList.selectResponder.accept(this.id);
                soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            else {
                Debug.error("There is no accept responder for Resource Location List. Could not accept location: " + this.id);
            }
        }

        // For easier backport I hope
        private int x() {
            return this.getX();
        }

        private int y() {
            return this.getY();
        }

        private int width() {
            return this.getWidth();
        }

        private int height() {
            return this.getHeight();
        }
        //

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
            Color mainColor = Color.WHITE;
            Color secondColor = Color.LIGHT_GRAY;

            if (isFocused()) {
                mainColor = Color.YELLOW;
                secondColor = Color.PURPLE;
            }
            else if (isHovering) {
                var context = new GuiContext(guiGraphics);
                context.fill(x(), y(), width(), height(), Color.WHITE.withAlpha(16).getARGB());
                context.fill(x() + 1, y() + 1, width() - 2, height() - 2, Color.BLACK.withAlpha(64).getARGB());
            }

            int nameX = x() + 3;
            int nameY = y() + 5;
            FormattedCharSequence nameFirstLine = nameLines.getFirst();

            if (nameLines.size() == 1) {
                guiGraphics.drawString(font, nameFirstLine, nameX, nameY, mainColor.getARGB());
            }
            else {
                guiGraphics.drawString(font, nameLines.get(1), nameX, nameY + 3, secondColor.getARGB());
                guiGraphics.drawString(font, nameFirstLine, nameX, nameY - 3, mainColor.getARGB());
            }

            if (isConfig) {
                guiGraphics.drawString(font, Component.literal("c"), x() + width() - 10, nameY, Color.LIGHT_GRAY.getARGB());
            }
        }
    }
}
