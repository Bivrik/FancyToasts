package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
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

    private enum VisualSettingType {
        TEXTURES,
        ANIMATIONS,
        SOUNDS;
    }
    private VisualSettingType visualSettingType = VisualSettingType.TEXTURES;

    public FancyToastConfigScreen(String title, Screen parent) {
        super(Component.literal(title));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button doneButton = Button.builder(
                CommonComponents.GUI_DONE,
                (button) -> {
                    Objects.requireNonNull(this.minecraft).setScreen(parent);
                }
        ).bounds(this.width / 2 + 100 - 125 + 4, this.height - 26, 150, 20).build();
        this.addRenderableWidget(doneButton);

        Button backButton = Button.builder(
                CommonComponents.GUI_BACK,
                (button) -> {
                    Objects.requireNonNull(this.minecraft).setScreen(parent);
                }
        ).bounds(this.width / 2 - 125 - 4, this.height - 26, 100, 20).build();
        this.addRenderableWidget(backButton);

        CycleButton<VisualSettingType> visualSettingTypeCycleButton = CycleButton.builder(this::test)
                .displayOnlyValue()
                .withValues(VisualSettingType.values())
                .withInitialValue(visualSettingType)
                .create(
                        width - 128, 33,
                        120, 20,
                        Component.empty(),
                        (button, value) -> {
                            visualSettingType = value;

                            this.editBox.setValue("");
                            addResourceLocationsList();
                        });
        this.addRenderableWidget(visualSettingTypeCycleButton);

        addResourceLocationsList();

        this.editBox = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 60, 33, this.width / 2 - 60 - 8 - 8, 20, this.editBox, Component.literal("Test")));
        this.editBox.setResponder(list::onFilterUpdate);
    }

    private void addResourceLocationsList() {
        this.removeWidget(this.list);

        List<ResourceLocation> listContent = null;
        switch (visualSettingType) {
            case TEXTURES -> listContent = new ArrayList<>(ToastTextureRegistry.getIds());
            case ANIMATIONS -> listContent = new ArrayList<>(ToastAnimationRegistry.getIds());
            case SOUNDS -> listContent = new ArrayList<>(BuiltInRegistries.SOUND_EVENT.keySet());
        }

        this.list = this.addRenderableWidget(new ResourceLocationList(this.minecraft,
                this.width / 2 + 60 - 8, this.height - 28 - 33 - 33,
                this.width / 2 - 60, 28 + 33,
                18, listContent));
        this.list.setResponder(this::Yes);
    }

    private Component test(VisualSettingType visualSettingType) {
        return Component.translatable("fancytoasts.gui.label." + visualSettingType.toString().toLowerCase());
    }

    private void Yes(ResourceLocation t) {
        entry = t;
        Debug.message(t + " yes");
    }

    private ResourceLocation entry;
    private ResourceLocationList list;
    private EditBox editBox;

    @Override
    public void onClose() {
        Objects.requireNonNull(this.minecraft).setScreen(parent);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, "Customization Settings", this.width / 2, 12, -1);

        renderDarkBackground(guiGraphics, 8, 33, this.width / 2 - 60 - 8 - 8, this.height - 33 - 33);

        if (entry != null) {
            guiGraphics.drawString(this.font, entry.toLanguageKey(), 8, 33 + 9, -1);
        }
    }

    private void renderDarkBackground(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        var resourcelocation = ResourceLocation.withDefaultNamespace("textures/gui/menu_list_background.png");
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourcelocation, x, y, 0, 0, width, height, 32, 32);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, x, y, 0, 0, width, 2, 32, 2);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, x, y + height, 0, 0, width, 2, 32, 2);
    }
}
