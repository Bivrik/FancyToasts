package net.bivrik.fancytoasts.client.gui.screen;

import net.bivrik.fancytoasts.client.toast.Appearance;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.event.ToastConfigDataEvent;
import net.bivrik.fancytoasts.core.manager.CustomTextureManager;
import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.client.gui.InformationList;
import net.bivrik.fancytoasts.client.gui.ResourceLocationFilter;
import net.bivrik.fancytoasts.client.gui.ResourceLocationList;
import net.bivrik.fancytoasts.client.gui.SettingType;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class ToastConfigScreen extends UniversalScreen {
    private static final Component TITLE = Components.of("title.visual_settings");
    private static final Component SAVED_LABEL = Components.of("label.saved");
    private static final Component CONFIG_FOLDER = Components.of("gui.config_folder");
    private static final Component RELOAD_CUSTOMS = Components.of("gui.reload_customs");
    private static final Component RELOAD_CUSTOMS_TOOLTIP = Components.of("tooltip.reload_customs");

    private final ToastConfigData toastConfigData;
    private final CustomTextureManager customTextureManager;

    private ResourceLocationFilter filter = ResourceLocationFilter.A_Z;
    private SettingType settingType = SettingType.TEXTURES;
    private FancyToastType advancementType = FancyToastType.TASK;
    private DisplayData selectedDisplayData;
    private boolean isSaved;
    private long savedFeedbackStartTime;

    private Button doneButton;
    private Button backButton;
    private Button configsFolderButton;
    private Button reloadConfigsButton;
    private CycleButton<SettingType> settingTypeButton;
    private CycleButton<ResourceLocationFilter> locationsFilterButton;
    private CycleButton<FancyToastType> advancementTypeButton;
    private EditBox editBox;
    private ResourceLocationList locationsList;
    private InformationList informationList;

    public ToastConfigScreen(Screen parent) {
        super(TITLE, parent);

        this.toastConfigData = Managers.getConfigManager().getToastConfigData();
        this.customTextureManager = Managers.getCustomTextureManager();
        this.customTextureManager.reload();

        this.selectedDisplayData = this.settingType.getDisplayData(this.toastConfigData.getTextureId());
    }

    public FancyToastType getAdvancementType() {
        return advancementType;
    }

    public ToastConfigData getConfigData() {
        return toastConfigData;
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2;
        int yFirstRowBottom = this.height - 26;
        int ySecondRowBottom = yFirstRowBottom - BUTTON_HEIGHT - PADDING;

        // Bottom buttons first row
        backButton = this.addFWidget(createButton(CommonComponents.GUI_BACK, button -> this.toParentScreen(),
                xCenter - 100 - 25 - HALF_PADDING, yFirstRowBottom, 100, BUTTON_HEIGHT));

        doneButton = this.addFWidget(createButton(CommonComponents.GUI_DONE, button -> done(),
                xCenter - 25 + HALF_PADDING, yFirstRowBottom));

        // Locations list for entries from setting type (textures, animations, sounds)
        locationsList = this.addFWidget(new ResourceLocationList(this.minecraft,
                xCenter + 60 - PADDING, this.height - 20 - PADDING - MARGIN * 2 - 2 - BUTTON_HEIGHT - PADDING,
                xCenter - 60, 20 + PADDING + MARGIN,
                18, settingType));
        locationsList.setSelectResponder(this::onSelectedEntry).setFocusResponder(this::onFocusedEntry);

        // Location list's filtering buttons
        editBox = this.addFWidget(new EditBox(this.font, xCenter - 60, MARGIN, xCenter - 40 - 30 - PADDING * 4, BUTTON_HEIGHT, this.editBox, Component.empty()));
        editBox.setResponder(locationsList::setSearch);

        locationsFilterButton = this.addFWidget(CycleButton.builder(ResourceLocationFilter::getDisplayName).displayOnlyValue()
                .withValues(ResourceLocationFilter.values())
                .create(this.width - 80 - PADDING * 2 - 60, MARGIN, 60, BUTTON_HEIGHT, Component.empty(), (button, value) -> setFilter(value, button)));

        // Information list for description about entry
        informationList = this.addFWidget(new InformationList(this.minecraft,
                xCenter - 60 - PADDING * 2, this.height - MARGIN * 2 - 2 - BUTTON_HEIGHT - PADDING,
                PADDING, MARGIN,
                selectedDisplayData, settingType.getCurrentId(this).getPath().contains(Constants.CONFIG)));

        // Button to change resource location's content and setting to change
        settingTypeButton = this.addFWidget(CycleButton.builder(SettingType::getDisplayName).displayOnlyValue()
                .withValues(SettingType.values())
                .withTooltip(settingType -> getTooltip(settingType.getName()))
                .create(this.width - 88, MARGIN, 80, BUTTON_HEIGHT, Component.empty(), (button, value) -> setSettingType(value)));

        // Bottom buttons second row
        configsFolderButton = this.addFWidget(createButton(CONFIG_FOLDER, button -> openConfigsFolder(),
                xCenter - 86 / 2 - PADDING, ySecondRowBottom, 86, BUTTON_HEIGHT));

        reloadConfigsButton = this.addFWidget(createButton(RELOAD_CUSTOMS, button -> reloadCustomTextures(),
                xCenter + 129 - 86, ySecondRowBottom, 86, BUTTON_HEIGHT, Tooltip.create(RELOAD_CUSTOMS_TOOLTIP)));

        advancementTypeButton = this.addFWidget(CycleButton.builder(FancyToastType::getDisplayName).displayOnlyValue()
                .withValues(FancyToastType.values())
                .withTooltip(advancementType -> getTooltip(advancementType.getName()))
                .create(xCenter - 129, ySecondRowBottom, 70, BUTTON_HEIGHT, Component.empty(), (button, value) -> setAdvancementType(value)));
        tryToggleAdvancementTypeButton();
    }

    private Tooltip getTooltip(String valueName) {
        return Tooltip.create(Components.of("tooltip." + valueName));
    }

    private void openConfigsFolder() {
        Util.getPlatform().openPath(Paths.actualPath(Paths.CONFIG));
    }

    private void reloadCustomTextures() {
        customTextureManager.reload();
        locationsList.setResourceLocations(settingType);
    }

    private void done() {
        ToastConfigData data = toastConfigData.copy();
        if (!data.equals(Managers.getConfigManager().getToastConfigData())) {
            save(data);
        } else {
            this.toParentScreen();
        }
    }

    private void save(ToastConfigData data) {
        ResourceLocation textureId = data.getTextureId();

        customTextureManager.releaseUnusedTexturesFromMinecraft();
        if (textureId.toLanguageKey().contains(Constants.CONFIG)) {
            customTextureManager.registerInMinecraft(textureId);
        }

        ConfigHandler.save(data);
        Managers.getEventManager().changed(new ToastConfigDataEvent(data));
        isSaved = true;
        savedFeedbackStartTime = Util.getMillis();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        drawSavedFeedback(guiGraphics, this.width / 2 + PADDING - 25 + BUTTON_WIDTH, this.height - BUTTON_HEIGHT);
    }

    private void drawSavedFeedback(GuiGraphics guiGraphics, int x, int y) {
        if (!isSaved) {
            return;
        }
        long time = Util.getMillis() - savedFeedbackStartTime;

        float appearanceLerp = MathEasing.easeOutLerp(0.0f, 1.0f, Appearance.getProgress(time, 500, 0));
        float disappearanceLerp = Appearance.getProgress(time, 500, 400);

        int color = Colors.alpha(appearanceLerp - disappearanceLerp, Colors.YELLOW);

        guiGraphics.drawString(this.font, SAVED_LABEL, x, y, color);

        if (time >= 1000) {
            isSaved = false;
        }
    }

    private <T extends GuiEventListener> void setFilter(ResourceLocationFilter filter, T button) {
        this.filter = filter;

        if (button == null) {
            locationsFilterButton.setValue(this.filter);
        }
        locationsList.setFilter(this.filter);
    }

    private void setSettingType(SettingType settingType) {
        this.settingType = settingType;
        locationsList.setResourceLocations(this.settingType);

        resetFiltering();
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        resetFiltering();
    }

    private void resetFiltering() {
        editBox.setValue("");
        setFilter(ResourceLocationFilter.A_Z, null);
        tryToggleAdvancementTypeButton();
    }

    private void tryToggleAdvancementTypeButton() {
        advancementTypeButton.active = settingType == SettingType.SOUNDS;

        informationList$updateOnReload();
    }

    private void setAdvancementType(FancyToastType type) {
        advancementType = type;

        informationList$updateOnReload();
    }

    private void informationList$updateOnReload() {
        informationList.update(settingType.getDisplayData(getCurrentId()), getCurrentId().getPath().contains(Constants.CONFIG), true);
    }

    private void onSelectedEntry(ResourceLocation location) {
        settingType.apply(this, location);

        informationList$updateSelected(location, true);
    }

    private void onFocusedEntry(ResourceLocation location) {
        selectedDisplayData = settingType.getDisplayData(location);

        boolean isSelected = getCurrentId().equals(location);
        informationList$updateSelected(location, isSelected);
    }

    private void informationList$updateSelected(ResourceLocation location, boolean isSelected) {
        informationList.update(selectedDisplayData, location.getPath().contains(Constants.CONFIG), isSelected);
    }

    private ResourceLocation getCurrentId() {
        return settingType.getCurrentId(this);
    }
}
