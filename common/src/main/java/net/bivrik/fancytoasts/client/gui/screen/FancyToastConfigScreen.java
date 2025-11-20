package net.bivrik.fancytoasts.client.gui.screen;

import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class FancyToastConfigScreen extends UniversalScreen {
    private final String splash;

    private Button toastConfigButton;
    private Button generalConfigButton;
    private Button creditsScreenButton;
    private Button backButton;

    public FancyToastConfigScreen(Screen parent) {
        super(Components.of("gui.config.title"), parent);
        this.splash = Managers.splashManager().getSplash();
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2;
        int yCenter = this.height / 2;
        int halfButtonWidth = BUTTON_WIDTH / 2;

        backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> this.toParentScreen())
                .bounds(xCenter - halfButtonWidth, this.height - BUTTON_HEIGHT - 16, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        toastConfigButton = this.addRenderableWidget(Button.builder(Component.translatable("fancytoasts.gui.label.toast_settings"), (button) -> openToastConfigScreen())
                .bounds(xCenter - halfButtonWidth, yCenter - PADDING / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        generalConfigButton = this.addRenderableWidget(Button.builder(Component.translatable("fancytoasts.gui.label.general_settings"), (button) -> openGeneralConfigScreen())
                .bounds(xCenter - halfButtonWidth, yCenter + PADDING / 2 + BUTTON_HEIGHT / 2, halfButtonWidth - PADDING / 2, BUTTON_HEIGHT).build());

        creditsScreenButton = this.addRenderableWidget(Button.builder(Component.translatable("fancytoasts.gui.label.credits"), (button) -> openCreditsScreen())
                .bounds(xCenter + PADDING / 2, yCenter + PADDING / 2 + BUTTON_HEIGHT / 2, halfButtonWidth - PADDING / 2, BUTTON_HEIGHT).build());

        Component supportText = Component.translatable("fancytoasts.gui.support");
        int textWidth = this.font.width(supportText) + 1;
        this.addRenderableWidget(new PlainTextButton(this.width - textWidth, this.height - 10, textWidth, 10, supportText, ConfirmLinkScreen.confirmLink(this, URI.create("https://boosty.to/bivrik")), this.font));
    }

    private void openToastConfigScreen() {
        this.openScreen(new ToastConfigScreen(this));
    }

    private void openGeneralConfigScreen() {
        this.openScreen(new GeneralConfigScreen(this));
    }

    private void openCreditsScreen() {
        this.openScreen(new CreditsScreen(this));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        drawSplash(guiGraphics);
    }

    private void drawSplash(@NotNull GuiGraphics guiGraphics) {
        float size = (float) (Math.abs(Math.cos((double) Util.getMillis() / 250) * 0.1f) + 0.9f);

        GuiContext context = new GuiContext(guiGraphics);
        context.push();
        context.scaleAround(size, (float) (this.width / 2), 12 + 9 + 4.5F);
        guiGraphics.drawCenteredString(this.font, splash, this.width / 2, 12 + 9, Colors.YELLOW);
        context.pop();
    }
}
