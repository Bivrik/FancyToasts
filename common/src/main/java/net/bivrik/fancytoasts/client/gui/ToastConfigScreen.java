package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.ConfigTextureManager;
import net.bivrik.fancytoasts.client.config.ToastConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.toast.texture.DisplayData;
import net.minecraft.advancements.AdvancementType;
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

    private SettingType settingType = SettingType.TEXTURES;
    private AdvancementType advancementType = AdvancementType.TASK;
    private DisplayData displayData = settingType.getDisplayData(Common.getConfigManager().getToastConfig().getTextureId());

    private Button doneButton;
    private Button backButton;
    private CycleButton<SettingType> settingTypeCycButton;
    private CycleButton<AdvancementType> advancementTypeCycButton;
    private EditBox editBox;
    private ResourceLocationList locationsList;
    private InformationList infoList;

    public ToastConfigScreen(Component title, Screen parent) {
        super(title, parent);
        this.toastConfigData = Common.getConfigManager().getToastConfig();
        ConfigTextureManager.reload();
    }

    public AdvancementType getAdvancementType() {
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
                .create(this.width - 128, MARGIN, 120, BUTTON_HEIGHT, Component.empty(), (button, value) -> setSettingType(value)));

        advancementTypeCycButton = CycleButton.builder(this::getAdvancementTypeDisplayName).displayOnlyValue()
                .withValues(AdvancementType.values()).withInitialValue(advancementType)
                .withTooltip((advancementType) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip." + advancementType.getSerializedName())))
                .create(PADDING, MARGIN, 120, BUTTON_HEIGHT, Component.empty(), (button, value) -> setAdvancementType(value));

        locationsList = this.addRenderableWidget(new ResourceLocationList(this.minecraft,
                this.width / 2 + 60 - PADDING, this.height - 20 - PADDING - MARGIN * 2 - 2,
                this.width / 2 - 60, 20 + PADDING + MARGIN,
                18, settingType));
        locationsList.setAcceptResponder(this::onAcceptedEntry);
        locationsList.setSelectResponder(this::onSelectedEntry);

        infoList = this.addRenderableWidget(new InformationList(this.minecraft,
                this.width / 2 - 60 - PADDING * 2, this.height - 20 - PADDING - MARGIN * 2 - 2,
                PADDING, 20 + PADDING + MARGIN,
                displayData, settingType.getCurrentId(this).toLanguageKey().contains("config")));

        editBox = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 60, MARGIN, this.width / 2 - 60 - PADDING * 2, BUTTON_HEIGHT, this.editBox, Component.literal("...")));
        editBox.setResponder(locationsList::onFilterUpdate);

        tryAddAdvancementTypeCycButton();
    }

    private Component getAdvancementTypeDisplayName(AdvancementType type) {
        return Component.translatable("fancytoasts.gui.label." + type.getSerializedName());
    }

    private void done() {
        ConfigHandler.save(toastConfigData);
        this.toParentScreen();
    }

    @Override
    protected void toParentScreen() {
        ResourceLocation textureId = toastConfigData.getTextureId();
        if (textureId.toLanguageKey().contains("config")) {
            ConfigTextureManager.registerInMinecraft(textureId);
        }

        super.toParentScreen();
    }

    private void setSettingType(SettingType type) {
        settingType = type;
        editBox.setValue("");
        locationsList.setResourceLocations(settingType);
        tryAddAdvancementTypeCycButton();
    }

    private void tryAddAdvancementTypeCycButton() {
        if (settingType == SettingType.SOUNDS) {
            advancementType = AdvancementType.TASK;
            advancementTypeCycButton.setValue(advancementType);
            this.addRenderableWidget(this.advancementTypeCycButton);
        }
        else {
            this.removeWidget(this.advancementTypeCycButton);
        }

        infoList.update(settingType.getDisplayData(settingType.getCurrentId(this)), settingType.getCurrentId(this).toLanguageKey().contains("config"));
    }

    private void setAdvancementType(AdvancementType type) {
        advancementType = type;
        infoList.update(settingType.getDisplayData(settingType.getCurrentId(this)), settingType.getCurrentId(this).toLanguageKey().contains("config"));
    }

    private void onAcceptedEntry(ResourceLocation location) {
        settingType.apply(this, location);
        infoList.update(displayData, settingType.getCurrentId(this).toLanguageKey().contains("config"));
    }

    private void onSelectedEntry(ResourceLocation location) {
        displayData = settingType.getDisplayData(location);
        infoList.update(displayData, location.toLanguageKey().contains("config"));
    }
}
