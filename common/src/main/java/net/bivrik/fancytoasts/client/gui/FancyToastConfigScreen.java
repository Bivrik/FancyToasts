package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.utility.Colors;
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
import java.util.Objects;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class FancyToastConfigScreen extends Screen {
    private final Screen parent;
    private final String splash;

    private Button toastConfigButton;
    private Button generalConfigButton;
    private Button backButton;

    public FancyToastConfigScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
        this.splash = Common.getSplashManager().getSplash();
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2;
        int yCenter = this.height / 2;
        int halfButtonWidth = BUTTON_WIDTH / 2;

        backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> toParentScreen())
                .bounds(xCenter - halfButtonWidth, this.height - BUTTON_HEIGHT - 6, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        toastConfigButton = this.addRenderableWidget(Button.builder(Component.translatable("fancytoasts.gui.label.toast_settings"), (button) -> openToastConfigScreen())
                .bounds(xCenter - halfButtonWidth, yCenter - BUTTON_HEIGHT - PADDING / 2, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        generalConfigButton = this.addRenderableWidget(Button.builder(Component.translatable("fancytoasts.gui.label.general_settings"), (button) -> openGeneralConfigScreen())
                .bounds(xCenter - halfButtonWidth, yCenter + PADDING / 2, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        Component supportText = Component.translatable("fancytoasts.gui.support");
        int textWidth = this.font.width(supportText) + 1;
        this.addRenderableWidget(new PlainTextButton(this.width - textWidth, this.height - 10, textWidth, 10, supportText, ConfirmLinkScreen.confirmLink(this, URI.create("https://boosty.to/bivrik")), this.font));
    }

    private void openToastConfigScreen() {
        Objects.requireNonNull(this.minecraft).setScreen(new ToastConfigScreen(Component.translatable("fancytoasts.gui.config.customization_title"), this));
    }

    private void openGeneralConfigScreen() {
        Objects.requireNonNull(this.minecraft).setScreen(new GeneralConfigScreen(Component.translatable("fancytoasts.gui.config.general_title"), this));
    }

    private void toParentScreen() {
        Objects.requireNonNull(this.minecraft).setScreen(parent);
    }

    @Override
    public void onClose() {
        toParentScreen();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.pose().pushMatrix();
        double size = Math.abs(Math.cos((double) Util.getMillis() / 250) * 0.1f) + 0.9f;
        guiGraphics.pose().translate((float) this.width / 2, 12 + 9 + 4.5f);
        guiGraphics.pose().scale((float) size);
        guiGraphics.pose().translate((float) this.width / -2, -12 - 9 - 4.5f);
        guiGraphics.drawCenteredString(this.font, splash, this.width / 2, 12 + 9, Colors.YELLOW);
        guiGraphics.pose().popMatrix();

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 12, -1);
    }
}
