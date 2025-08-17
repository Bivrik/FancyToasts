package net.bivrik.fancytoasts.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.toast.animation.AnimationType;
import net.bivrik.fancytoasts.config.ConfigHandler;
import net.bivrik.fancytoasts.texture.TextureType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.CommonColors;
import org.jetbrains.annotations.NotNull;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private AnimationType animationType;
    private TextureType textureType;

    public ConfigScreen(String title, Screen parent) {
        super(Component.literal(title));

        this.parent = parent;
        animationType = Common.CONFIG.getAnimationType();
        textureType = Common.CONFIG.getTextureType();
    }

    private String getTranslatableType(String enumType) {
        return "fancytoasts.gui." + enumType.toLowerCase();
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(
                Component.translatable(getTranslatableType(animationType.toString())),
                button -> {
                    switch (animationType) {
                        case STANDARD -> animationType = AnimationType.PLAYFUL;
                        case PLAYFUL -> animationType = AnimationType.STANDARD;
                    }
                    button.setMessage(Component.translatable(getTranslatableType(animationType.toString())));
                }
        ).bounds(width / 2 - 60, 50, 120, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable(getTranslatableType(textureType.toString())),
                (button) -> {
                    switch (textureType) {
                        case VANILLA -> textureType = TextureType.NATURE;
                        case NATURE -> textureType = TextureType.OG;
                        case OG -> textureType = TextureType.MODERN;
                        case MODERN -> textureType = TextureType.VANILLA;
                    }
                    button.setMessage(Component.translatable(getTranslatableType(textureType.toString())));
                }
        ).bounds(width / 2 - 60, 80, 120, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable("fancytoasts.gui.save_changes"),
                (button) -> {
                    ConfigHandler.save(animationType, textureType);
                    openPreviousScreen();
                }
        ).bounds(width / 2 + 10, height - 40, 120, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable("fancytoasts.gui.close"),
                (button) -> {
                    openPreviousScreen();
                }
        ).bounds(width / 2 - 130, height - 40, 120, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.drawCenteredString(this.font, Component.translatable("fancytoasts.gui.config_title"), this.width / 2, 20, CommonColors.WHITE);

        MutableComponent textAnimationType = Component.translatable("fancytoasts.gui.animation_type");
        graphics.drawString(this.font, textAnimationType, this.width / 2 - this.font.width(textAnimationType) - 70, 55, CommonColors.WHITE, true);

        MutableComponent textTextureType = Component.translatable("fancytoasts.gui.texture_type");
        graphics.drawString(this.font, textTextureType, this.width / 2 - this.font.width(textTextureType) - 70, 85, CommonColors.WHITE, true);
    }

    private void openPreviousScreen() {
        assert this.minecraft != null;

        this.minecraft.setScreen(this.parent);
    }
}
