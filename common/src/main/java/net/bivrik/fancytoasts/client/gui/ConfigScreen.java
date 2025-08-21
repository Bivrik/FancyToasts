package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.toast.animation.AnimationType;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import org.jetbrains.annotations.NotNull;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private AnimationType animationType;
    private ResourceLocation textureId;

    public ConfigScreen(String title, Screen parent) {
        super(Component.literal(title));

        this.parent = parent;
        animationType = Common.CONFIG.getAnimationType();
        textureId = Common.CONFIG.getTextureId();
    }

    private String getTranslatableType(String enumType) {
        return "fancytoasts.gui." + enumType.toLowerCase();
    }

    @Override
    protected void init() {
        // Texture cycle button
        CycleButton<ResourceLocation> textureCycleButton = CycleButton.builder(ToastTextureRegistry::getTextureName)
                .withValues(ToastTextureRegistry.getIds())
                .withInitialValue(textureId)
                .create(
                        width / 2 - 60, 80,
                        120, 20,
                        Component.empty(),
                        (button, value) -> {
                            button.setMessage(ToastTextureRegistry.getTextureName(value));
                            textureId = value;
                        });

        // Animation cycle button
        Button animationCycleButton = Button.builder(
                Component.translatable(getTranslatableType(animationType.toString())),
                button -> {
                    switch (animationType) {
                        case STANDARD -> animationType = AnimationType.PLAYFUL;
                        case PLAYFUL -> animationType = AnimationType.STANDARD;
                    }
                    button.setMessage(Component.translatable(getTranslatableType(animationType.toString())));
                }
        ).bounds(width / 2 - 60, 50, 120, 20).build();

        // Save button
        Button saveButton = Button.builder(
                Component.translatable("fancytoasts.gui.save_changes"),
                (button) -> {
                    ConfigHandler.save(animationType, textureId);
                    openPreviousScreen();
                }
        ).bounds(width / 2 + 10, height - 40, 120, 20).build();

        // Cancel button
        Button cancelButton = Button.builder(
                Component.translatable("fancytoasts.gui.close"),
                (button) -> {
                    openPreviousScreen();
                }
        ).bounds(width / 2 - 130, height - 40, 120, 20).build();

        this.addRenderableWidget(textureCycleButton);
        this.addRenderableWidget(animationCycleButton);
        this.addRenderableWidget(saveButton);
        this.addRenderableWidget(cancelButton);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        // Title
        graphics.drawCenteredString(this.font, Component.translatable("fancytoasts.gui.config_title"), this.width / 2, 20, CommonColors.WHITE);

        // Animation text
        MutableComponent textAnimationType = Component.translatable("fancytoasts.gui.animation_type");
        graphics.drawString(this.font, textAnimationType, this.width / 2 - this.font.width(textAnimationType) - 70, 55, CommonColors.WHITE, true);

        // Texture text
        MutableComponent textTextureType = Component.translatable("fancytoasts.gui.texture_type");
        graphics.drawString(this.font, textTextureType, this.width / 2 - this.font.width(textTextureType) - 70, 85, CommonColors.WHITE, true);
    }

    private void openPreviousScreen() {
        assert this.minecraft != null;

        this.minecraft.setScreen(this.parent);
    }
}
