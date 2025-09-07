package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.ConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
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
    private ResourceLocation entry;

    private Button doneButton;
    private Button backButton;
    private CycleButton<SettingType> settingTypeCycleButton;
    private CycleButton<AdvancementType> advancementTypeCycleButton;
    private EditBox editBox;
    private ResourceLocationList list;

    private final int PADDING = 8;
    private final int MARGIN = 33;
    private final int BUTTON_HEIGHT = 20;
    private final int BUTTON_WIDTH = 150;

    public FancyToastConfigScreen(String title, Screen parent) {
        super(Component.literal(title));
        this.parent = parent;
        configData = Common.CONFIG;
    }

    @Override
    protected void init() {
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.done())
                .bounds(this.width / 2 + 100 - 125 + 4, this.height - 26, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        this.backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> this.toParentScreen())
                .bounds(this.width / 2 - 125 - 4, this.height - 26, 100, BUTTON_HEIGHT).build());

        this.settingTypeCycleButton = this.addRenderableWidget(CycleButton.builder(SettingType::getDisplayName).displayOnlyValue()
                .withValues(SettingType.values()).withInitialValue(settingType)
                .create(width - 128, MARGIN, 120, BUTTON_HEIGHT, Component.empty(), (button, value) -> this.setSettingType(value)));

        this.advancementTypeCycleButton = CycleButton.builder(AdvancementType::getDisplayName).displayOnlyValue()
                .withValues(AdvancementType.values()).withInitialValue(advancementType)
                .create(width / 2 - 128, MARGIN, 120, BUTTON_HEIGHT, Component.empty(), (button, value) -> this.setAdvancementType(value));

        this.list = this.addRenderableWidget(new ResourceLocationList(this.minecraft,
                this.width / 2 + 60 - PADDING, this.height - 28 - MARGIN * 2,
                this.width / 2 - 60, 28 + MARGIN,
                18, settingType.getKeySet()));
        this.list.setResponder(this::onSelectedEntry);

        this.editBox = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 60, MARGIN, this.width / 2 - 60 - PADDING * 2, BUTTON_HEIGHT, this.editBox, Component.literal("Test")));
        this.editBox.setResponder(list::onFilterUpdate);
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
        this.list.fillList(settingType.getKeySet());

        if (type == SettingType.SOUNDS) {
            advancementType = AdvancementType.TASK;
            this.addRenderableWidget(this.advancementTypeCycleButton);
        }
        else {
            this.removeWidget(this.advancementTypeCycleButton);
        }
    }

    private void setAdvancementType(AdvancementType type) {
        advancementType = type;
    }

    @Override
    public void onClose() {
        toParentScreen();
    }

    private void onSelectedEntry(ResourceLocation entry) {
        settingType.apply(this, entry);
        this.entry = entry;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, "Customization Settings", this.width / 2, 12, -1);

        renderDarkBackground(guiGraphics, PADDING, MARGIN, this.width / 2 - 60 - PADDING * 2, this.height - MARGIN * 2);

        if (entry != null) {
            guiGraphics.drawString(this.font, entry.toLanguageKey(), PADDING, MARGIN + 9, -1);
        }
    }

    private void renderDarkBackground(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        var resourcelocation = ResourceLocation.withDefaultNamespace("textures/gui/menu_list_background.png");
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourcelocation, x, y, 0, 0, width, height, 32, 32);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, x, y, 0, 0, width, 2, 32, 2);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, x, y + height, 0, 0, width, 2, 32, 2);
    }

    private enum SettingType {
        TEXTURES("textures") {
            @Override
            void apply(FancyToastConfigScreen instance, ResourceLocation entry) {
                instance.configData.setTextureId(entry);
            }

            @Override
            ResourceLocation[] getKeySet() {
                return ToastTextureRegistry.getIds().toArray(new ResourceLocation[0]);
            }
        },
        ANIMATIONS("animations") {
            @Override
            void apply(FancyToastConfigScreen instance, ResourceLocation entry) {
                instance.configData.setAnimationId(entry);
            }

            @Override
            ResourceLocation[] getKeySet() {
                return ToastAnimationRegistry.getIds().toArray(new ResourceLocation[0]);
            }
        },
        SOUNDS("sounds") {
            @Override
            void apply(FancyToastConfigScreen instance, ResourceLocation entry) {
                instance.configData.putSound(instance.advancementType, entry);
            }

            @Override
            ResourceLocation[] getKeySet() {
                return BuiltInRegistries.SOUND_EVENT.keySet().toArray(new ResourceLocation[0]);
            }
        };

        abstract void apply(FancyToastConfigScreen instance, ResourceLocation entry);
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
