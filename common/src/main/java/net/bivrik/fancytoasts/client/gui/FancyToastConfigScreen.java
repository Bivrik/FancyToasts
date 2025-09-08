package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.config.ConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureData;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FancyToastConfigScreen extends Screen {
    private final Screen parent;
    private final ConfigData configData;

    private SettingType settingType = SettingType.TEXTURES;
    private AdvancementType advancementType = AdvancementType.TASK;
    private ToastTextureData displayData = settingType.getDisplayData(Common.CONFIG.getTextureId());

    private Button doneButton;
    private Button backButton;
    private CycleButton<SettingType> settingTypeCycleButton;
    private CycleButton<AdvancementType> advancementTypeCycleButton;
    private EditBox editBox;
    private ResourceLocationList resLocList;
    private InformationList infoList;

    private final int PADDING = 8;
    private final int MARGIN = 33;
    private final int BUTTON_HEIGHT = 20;
    private final int BUTTON_WIDTH = 150;

    public FancyToastConfigScreen(String title, Screen parent) {
        super(Component.literal(title));
        this.parent = parent;
        configData = Common.CONFIG.get();
    }

    @Override
    protected void init() {
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.done())
                .bounds(this.width / 2 + 100 - 125 + PADDING / 2, this.height - 26, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        this.backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> this.toParentScreen())
                .bounds(this.width / 2 - 125 - PADDING / 2, this.height - 26, 100, BUTTON_HEIGHT).build());

        this.settingTypeCycleButton = this.addRenderableWidget(CycleButton.builder(SettingType::getDisplayName).displayOnlyValue()
                .withValues(SettingType.values()).withInitialValue(settingType)
                .create(width - 128, MARGIN, 120, BUTTON_HEIGHT, Component.empty(), (button, value) -> this.setSettingType(value)));

        this.advancementTypeCycleButton = CycleButton.builder(this::getAdvancementTypeDisplayName).displayOnlyValue()
                .withValues(AdvancementType.values()).withInitialValue(advancementType)
                .create(PADDING, MARGIN, 120, BUTTON_HEIGHT, Component.empty(), (button, value) -> this.setAdvancementType(value));

        this.resLocList = this.addRenderableWidget(new ResourceLocationList(this.minecraft,
                this.width / 2 + 60 - PADDING, this.height - 28 - MARGIN * 2,
                this.width / 2 - 60, 28 + MARGIN,
                18, settingType.getKeySet()));
        this.resLocList.setAcceptResponder(this::onAcceptedEntry);
        this.resLocList.setSelectResponder(this::onSelectedEntry);

        this.infoList = this.addRenderableWidget(new InformationList(this.minecraft,
                this.width / 2 - 60 - PADDING * 2, this.height - 28 - MARGIN * 2,
                PADDING, 28 + MARGIN,
                this.displayData, settingType.getCurrentId(this).toLanguageKey().contains("config")));
        this.editBox = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 60, MARGIN, this.width / 2 - 60 - PADDING * 2, BUTTON_HEIGHT, this.editBox, Component.literal("Test")));
        this.editBox.setResponder(resLocList::onFilterUpdate);

        this.tryAddAdvancementTypeCycleButton();
    }

    private Component getAdvancementTypeDisplayName(AdvancementType type) {
        return Component.translatable("fancytoasts.gui.label." + type.getSerializedName());
    }

    private void done() {
        ConfigHandler.save(configData);
        toParentScreen();
    }

    private void toParentScreen() {
        Objects.requireNonNull(this.minecraft).setScreen(parent);
    }

    private void setSettingType(SettingType type) {
        settingType = type;
        this.editBox.setValue("");
        this.resLocList.setResourceLocations(settingType.getKeySet());
        this.tryAddAdvancementTypeCycleButton();
    }

    private void tryAddAdvancementTypeCycleButton() {
        if (settingType == SettingType.SOUNDS) {
            advancementType = AdvancementType.TASK;
            this.advancementTypeCycleButton.setValue(advancementType);
            this.addRenderableWidget(this.advancementTypeCycleButton);
        }
        else {
            this.removeWidget(this.advancementTypeCycleButton);
        }

        this.infoList.update(settingType.getDisplayData(settingType.getCurrentId(this)), settingType.getCurrentId(this).toLanguageKey().contains("config"));
    }

    private void setAdvancementType(AdvancementType type) {
        advancementType = type;
        this.infoList.update(settingType.getDisplayData(settingType.getCurrentId(this)), settingType.getCurrentId(this).toLanguageKey().contains("config"));
    }

    @Override
    public void onClose() {
        toParentScreen();
    }

    private void onAcceptedEntry(ResourceLocation location) {
        settingType.apply(this, location);
        this.infoList.update(displayData, settingType.getCurrentId(this).toLanguageKey().contains("config"));
    }

    private void onSelectedEntry(ResourceLocation location) {
        this.displayData = settingType.getDisplayData(location);
        this.infoList.update(displayData, location.toLanguageKey().contains("config"));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, Component.translatable("fancytoasts.gui.config.customization_title"), this.width / 2, 12, -1);
    }

    private enum SettingType {
        TEXTURES("textures") {
            @Override
            void apply(FancyToastConfigScreen instance, ResourceLocation id) {
                instance.configData.setTextureId(id);
            }

            @Override
            ToastTextureData getDisplayData(ResourceLocation id) {
                return ToastTextureRegistry.getData(id);
            }

            @Override
            ResourceLocation getCurrentId(FancyToastConfigScreen instance) {
                return instance.configData.getTextureId();
            }

            @Override
            ResourceLocation[] getKeySet() {
                return ToastTextureRegistry.getIds().toArray(new ResourceLocation[0]);
            }
        },
        ANIMATIONS("animations") {
            @Override
            void apply(FancyToastConfigScreen instance, ResourceLocation id) {
                instance.configData.setAnimationId(id);
            }

            @Override
            ToastTextureData getDisplayData(ResourceLocation id) {
                return ToastAnimationRegistry.getData(id);
            }

            @Override
            ResourceLocation getCurrentId(FancyToastConfigScreen instance) {
                return instance.configData.getAnimationId();
            }

            @Override
            ResourceLocation[] getKeySet() {
                return ToastAnimationRegistry.getIds().toArray(new ResourceLocation[0]);
            }
        },
        SOUNDS("sounds") {
            @Override
            void apply(FancyToastConfigScreen instance, ResourceLocation id) {
                instance.configData.putSound(instance.advancementType, id);
            }

            @Override
            ToastTextureData getDisplayData(ResourceLocation id) {
                return new ToastTextureData(Component.translatable(id.toLanguageKey()), "Minecraft", Component.translatable("Built-in sound from Minecraft"));
            }

            @Override
            ResourceLocation getCurrentId(FancyToastConfigScreen instance) {
                return instance.configData.getSoundId(instance.advancementType);
            }

            @Override
            ResourceLocation[] getKeySet() {
                return BuiltInRegistries.SOUND_EVENT.keySet().toArray(new ResourceLocation[0]);
            }
        };

        abstract void apply(FancyToastConfigScreen instance, ResourceLocation entry);
        abstract ToastTextureData getDisplayData(ResourceLocation id);
        abstract ResourceLocation getCurrentId(FancyToastConfigScreen instance);
        abstract ResourceLocation[] getKeySet();

        private final String name;

        SettingType(String name) {
            this.name = name;
        }

        public static Component getDisplayName(SettingType type) {
            return Component.translatable("fancytoasts.gui.label." + type.name);
        }
    }
}
