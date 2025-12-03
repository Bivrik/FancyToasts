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

public class FancyToastsScreen extends UniversalScreen {
    private static final int TITLE_BUTTON_WIDTH = 180;
    private static final int HALF_TITLE_BUTTON_WIDTH = TITLE_BUTTON_WIDTH / 2;

    private static final Component TITLE = Component.literal("Fancy Toasts");
    private static final Component SUPPORT_LABEL = Components.of("label.support");
    private static final Component TOAST_SETTINGS = Components.of("gui.toast_settings");
    private static final Component GENERAL_SETTINGS = Components.of("gui.general_settings");
    private static final Component TOASTS_FILTERING = Components.of("gui.toasts_filtering");
    private static final Component CREDITS = Components.of("gui.credits");

    private static final String BOOSTY_URI = "https://boosty.to/bivrik";

    private final String splash;

    private Button backButton;
    private Button toastConfigButton;
    private Button generalConfigButton;
    private Button toastsFilteringButton;
    private Button creditsButton;
    private PlainTextButton supportButton;

    public FancyToastsScreen(Screen parent) {
        super(TITLE, parent);

        this.splash = Managers.getSplashManager().getSplash();
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2 - HALF_TITLE_BUTTON_WIDTH;
        int yCenter = this.height / 2;
        int supportButtonWidth = this.font.width(SUPPORT_LABEL);

        toastConfigButton = this.addFWidget(this.createButton(TOAST_SETTINGS, button -> openToastConfigScreen(),
                xCenter, yCenter - BUTTON_HEIGHT - PADDING));

        generalConfigButton = this.addFWidget(this.createButton(GENERAL_SETTINGS, button -> openGeneralConfigScreen(),
                xCenter, yCenter));

        toastsFilteringButton = this.addFWidget(this.createButton(TOASTS_FILTERING, button -> openToastsFilteringScreen(),
                xCenter, yCenter + BUTTON_HEIGHT + PADDING, HALF_TITLE_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT));

        creditsButton = this.addFWidget(this.createButton(CREDITS, button -> openCreditsScreen(),
                xCenter + HALF_PADDING + HALF_TITLE_BUTTON_WIDTH, yCenter + BUTTON_HEIGHT + PADDING, HALF_TITLE_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT));

        backButton = this.addFWidget(this.createButton(CommonComponents.GUI_BACK, button -> this.toParentScreen(),
                xCenter, this.height - BUTTON_HEIGHT - 16));

        Button.OnPress supportButtonAction = ConfirmLinkScreen.confirmLink(BOOSTY_URI, this, true);
        supportButton = this.addFWidget(new PlainTextButton(this.width - supportButtonWidth - 1, this.height - 10, supportButtonWidth, 9, SUPPORT_LABEL, supportButtonAction, this.font));
    }

    @Override
    protected Button createButton(Component label, Button.OnPress action, int x, int y) {
        return super.createButton(label, action, x, y, TITLE_BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private void openToastConfigScreen() {
        this.openScreen(new ToastConfigScreen(this));
    }

    private void openGeneralConfigScreen() {
        this.openScreen(new GeneralConfigScreen(this));
    }

    private void openToastsFilteringScreen() {
        this.openScreen(new ToastsFilteringScreen(this));
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
