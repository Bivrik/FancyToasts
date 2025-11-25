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
    private static final Component TITLE = Components.of("gui.config.title");
    private static final Component TOAST_CONFIG_BUTTON_LABEL = Components.of("gui.label.toast_settings");
    private static final Component GENERAL_CONFIG_BUTTON_LABEL = Components.of("gui.label.general_settings");
    private static final Component CREDITS_BUTTON_LABEL = Components.of("gui.label.credits");
    private static final Component SUPPORT_BUTTON_LABEL = Components.of("gui.support");

    private static final URI BOOSTY_URI = URI.create("https://boosty.to/bivrik");

    private final String splash;

    private Button backButton;
    private Button toastConfigButton;
    private Button generalConfigButton;
    private Button creditsButton;
    private PlainTextButton supportButton;

    public FancyToastConfigScreen(Screen parent) {
        super(TITLE, parent);

        this.splash = Managers.getSplashManager().getSplash();
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2;
        int yCenter = this.height / 2;
        int supportButtonWidth = this.font.width(SUPPORT_BUTTON_LABEL);

        backButton = this.addFWidget(this.createButton(CommonComponents.GUI_BACK, button -> this.toParentScreen(),
                xCenter - HALF_BUTTON_WIDTH, this.height - BUTTON_HEIGHT - 16));

        toastConfigButton = this.addFWidget(this.createButton(TOAST_CONFIG_BUTTON_LABEL, button -> openToastConfigScreen(),
                xCenter - HALF_BUTTON_WIDTH, yCenter - HALF_PADDING - HALF_BUTTON_HEIGHT));

        generalConfigButton = this.addFWidget(this.createButton(GENERAL_CONFIG_BUTTON_LABEL, button -> openGeneralConfigScreen(),
                xCenter - HALF_BUTTON_WIDTH, yCenter + HALF_PADDING + HALF_BUTTON_HEIGHT, HALF_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT));

        creditsButton = this.addFWidget(this.createButton(CREDITS_BUTTON_LABEL, button -> openCreditsScreen(),
                xCenter + HALF_PADDING, yCenter + HALF_PADDING + HALF_BUTTON_HEIGHT, HALF_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT));

        Button.OnPress supportButtonAction = ConfirmLinkScreen.confirmLink(this, BOOSTY_URI);
        supportButton = this.addFWidget(new PlainTextButton(this.width - supportButtonWidth - 1, this.height - 10, supportButtonWidth, 9, SUPPORT_BUTTON_LABEL, supportButtonAction, this.font));
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
