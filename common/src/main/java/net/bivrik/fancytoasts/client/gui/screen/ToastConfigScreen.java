package net.bivrik.fancytoasts.client.gui.screen;

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
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class ToastConfigScreen extends UniversalScreen {
    private final ToastConfigData toastConfigData;
    private final CustomTextureManager customTextureManager;

    private SettingType settingType = SettingType.TEXTURES;
    private FancyToastType advancementType = FancyToastType.TASK;
    private DisplayData displayData;

    private Button doneButton;
    private Button backButton;
    private CycleButton<SettingType> settingTypeCycButton;
    private CycleButton<ResourceLocationFilter> resourceLocationFilterCycButton;
    private CycleButton<FancyToastType> advancementTypeCycButton;
    private EditBox editBox;
    private ResourceLocationList locationsList;
    private InformationList infoList;

    public ToastConfigScreen(Screen parent) {
        super(Components.of("gui.config.customization_title"), parent);

        this.toastConfigData = Managers.getConfigManager().getToastConfigData();

        this.customTextureManager = Managers.getCustomTextureManager();
        customTextureManager.reload();

        displayData = settingType.getDisplayData(toastConfigData.getTextureId());
    }

    public FancyToastType getAdvancementType() {
        return advancementType;
    }

    public ToastConfigData getConfigData() {
        return toastConfigData;
    }

    @Override
    protected void init() {
        doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> done())
                .bounds(this.width / 2 + 100 - 125 + PADDING / 2, this.height - 26, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> this.toParentScreen())
                .bounds(this.width / 2 - 125 - PADDING / 2, this.height - 20 - 6, 100, BUTTON_HEIGHT).build());

        settingTypeCycButton = this.addRenderableWidget(CycleButton.builder(SettingType::getDisplayName).displayOnlyValue()
                .withValues(SettingType.values()).withInitialValue(settingType)
                .withTooltip((settingType) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip." + settingType.getName())))
                .create(this.width - 88, MARGIN, 80, BUTTON_HEIGHT, Component.empty(), (button, value) -> setSettingType(value)));

        advancementTypeCycButton = CycleButton.builder(this::getAdvancementTypeDisplayName).displayOnlyValue()
                .withValues(FancyToastType.values()).withInitialValue(advancementType)
                .withTooltip((advancementType) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip." + advancementType.getName())))
                .create(PADDING, MARGIN, 80, BUTTON_HEIGHT, Component.empty(), (button, value) -> setAdvancementType(value));

        locationsList = this.addRenderableWidget(new ResourceLocationList(this.minecraft,
                this.width / 2 + 60 - PADDING, this.height - 20 - PADDING - MARGIN * 2 - 2,
                this.width / 2 - 60, 20 + PADDING + MARGIN,
                18, settingType));
        locationsList.setAcceptResponder(this::onAcceptedEntry);
        locationsList.setSelectResponder(this::onSelectedEntry);

        infoList = this.addRenderableWidget(new InformationList(this.minecraft,
                this.width / 2 - 60 - PADDING * 2, this.height - 20 - PADDING - MARGIN * 2 - 2,
                PADDING, 20 + PADDING + MARGIN,
                displayData, settingType.getCurrentId(this).toLanguageKey().contains(Constants.CONFIG)));

        editBox = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 60, MARGIN, this.width / 2 - 40 - 30 - PADDING * 4, BUTTON_HEIGHT, this.editBox, Component.literal("...")));
        editBox.setResponder(locationsList::onSearchUpdate);

        resourceLocationFilterCycButton = this.addRenderableWidget(CycleButton.builder(ResourceLocationFilter::getDisplayName).displayOnlyValue()
                .withValues(ResourceLocationFilter.values()).withInitialValue(filter)
                .create(this.width - 80 - PADDING * 2 - 60, MARGIN, 60, BUTTON_HEIGHT, Component.empty(), (button, value) -> setFilter(value)));

        setFilter(filter);
        tryAddAdvancementTypeCycButton();
    }

    private Component getAdvancementTypeDisplayName(FancyToastType type) {
        return Component.translatable("fancytoasts.gui.label." + type.getName());
    }

    private void done() {
        ResourceLocation textureId = toastConfigData.getTextureId();

        customTextureManager.releaseUnusedTexturesFromMinecraft();
        if (textureId.toLanguageKey().contains(Constants.CONFIG)) {
            customTextureManager.registerInMinecraft(textureId);
        }

        ConfigHandler.save(toastConfigData);
        Managers.getEventManager().changed(new ToastConfigDataEvent(toastConfigData));

        this.toParentScreen();
    }

    private ResourceLocationFilter filter = ResourceLocationFilter.A_Z;

    private void setFilter(ResourceLocationFilter filter) {
        editBox.setValue("");
        locationsList.onFilterUpdate(filter);
        this.filter = filter;
    }

    private void setSettingType(SettingType type) {
        settingType = type;
        editBox.setValue("");
        locationsList.setResourceLocations(settingType);
        locationsList.onFilterUpdate(filter);
        tryAddAdvancementTypeCycButton();
    }

    private void tryAddAdvancementTypeCycButton() {
        if (settingType == SettingType.SOUNDS) {
            advancementType = FancyToastType.TASK;
            advancementTypeCycButton.setValue(advancementType);
            this.addRenderableWidget(this.advancementTypeCycButton);
        }
        else {
            this.removeWidget(this.advancementTypeCycButton);
        }

        infoList.update(settingType.getDisplayData(settingType.getCurrentId(this)), settingType.getCurrentId(this).toLanguageKey().contains(Constants.CONFIG), true);
    }

    private void setAdvancementType(FancyToastType type) {
        advancementType = type;
        infoList.update(settingType.getDisplayData(settingType.getCurrentId(this)), settingType.getCurrentId(this).toLanguageKey().contains(Constants.CONFIG), true);
    }

    private void onAcceptedEntry(ResourceLocation location) {
        settingType.apply(this, location);
        infoList.update(displayData, settingType.getCurrentId(this).toLanguageKey().contains(Constants.CONFIG), true);
    }

    private void onSelectedEntry(ResourceLocation location) {
        displayData = settingType.getDisplayData(location);
        boolean isCurrent = settingType.getCurrentId(this).equals(location);
        infoList.update(displayData, location.toLanguageKey().contains(Constants.CONFIG), isCurrent);
    }
}
