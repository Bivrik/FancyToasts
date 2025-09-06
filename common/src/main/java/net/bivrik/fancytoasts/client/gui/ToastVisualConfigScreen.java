package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.ConfigTextureManager;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.util.TextureLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import org.jetbrains.annotations.NotNull;

public class ToastVisualConfigScreen extends Screen {
    private final Screen parent;
    private ResourceLocation animationId;
    private ResourceLocation textureId;
    private boolean jadeCompatibility;
    private final ResourceLocation initialTextureId;

    boolean isConfigTexture;

    public ToastVisualConfigScreen(String title, Screen parent) {
        super(Component.literal(title));

        this.parent = parent;
        animationId = Common.CONFIG.getAnimationId();
        textureId = Common.CONFIG.getTextureId();
        initialTextureId = textureId;
    }

    @Override
    protected void init() {
        ConfigTextureManager.reload();

        if (!ToastTextureRegistry.isRegistered(textureId)) {
            textureId = TextureLocations.VANILLA;
        }
        else {
            isConfigTexture = textureId.toString().contains("config");
        }

        // Texture cycle button
        CycleButton<ResourceLocation> textureCycleButton = CycleButton.builder(ToastTextureRegistry::getTextureName)
                .displayOnlyValue()
                .withValues(ToastTextureRegistry.getIds())
                .withInitialValue(textureId)
                .create(
                        width / 2 - 60, 80,
                        120, 20,
                        Component.empty(),
                        (button, value) -> {
                            textureId = value;
                            isConfigTexture = textureId.toString().contains("config");
                        });

        // Animation cycle button
        CycleButton<ResourceLocation> animationCycleButton = CycleButton.builder(ToastAnimationRegistry::getAnimationName)
                .displayOnlyValue()
                .withValues(ToastAnimationRegistry.getIds())
                .withInitialValue(animationId)
                .create(
                        width / 2 - 60, 50,
                        120, 20,
                        Component.empty(),
                        (button, value) -> {
                            animationId = value;
                        });

        // Save button
        Button saveButton = Button.builder(
                Component.translatable("fancytoasts.gui.save_changes"),
                (button) -> {
                    ConfigTextureManager.reload();
                    if (!ToastTextureRegistry.isRegistered(textureId)) {
                        textureId = TextureLocations.VANILLA;
                    }
                    else if (isConfigTexture) {
                        ConfigTextureManager.unregisterFromMinecraft(initialTextureId);
                        ConfigTextureManager.registerInMinecraft(textureId);
                    }

                    //ConfigHandler.save(animationId, textureId, );
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

        // Jade compatibility toggle button
        CycleButton<Boolean> jadeCompatToggle = CycleButton.onOffBuilder()
                .displayOnlyValue()
                .create(
                        width / 2 - 60, 150,
                        120, 20,
                        Component.literal("test"),
                        (button, bool) -> {
                            jadeCompatibility = bool;
                        }
                );

        this.addRenderableWidget(jadeCompatToggle);
        this.addRenderableWidget(textureCycleButton);
        this.addRenderableWidget(animationCycleButton);
        this.addRenderableWidget(saveButton);
        this.addRenderableWidget(cancelButton);
    }

    private <T extends GuiEventListener & Renderable & NarratableEntry> void updateWidget(T widget) {
        this.removeWidget(widget);
        this.addRenderableWidget(widget);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        // Custom texture display
        if (isConfigTexture) {
            graphics.drawString(this.font, Component.literal("Custom!"), this.width / 2 + 65, 87, CommonColors.LIGHT_GRAY);
            graphics.drawString(this.font, Component.literal("By: " + ToastTextureRegistry.getTextureAuthor(textureId).getString()), this.width / 2 - 60, 105, CommonColors.YELLOW);
        }

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
