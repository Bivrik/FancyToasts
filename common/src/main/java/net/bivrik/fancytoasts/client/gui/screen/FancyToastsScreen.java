package net.bivrik.fancytoasts.client.gui.screen;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class FancyToastsScreen extends UniversalScreen {
    private static final int TITLE_BUTTON_WIDTH = 200;
    private static final int HALF_TITLE_BUTTON_WIDTH = TITLE_BUTTON_WIDTH / 2;

    private static final Component TITLE = Component.literal("Fancy Toasts");
    private static final Component TOAST_SETTINGS = Components.of("gui.toast_settings");
    private static final Component GENERAL_SETTINGS = Components.of("gui.general_settings");
    private static final Component TOASTS_FILTERING = Components.of("gui.toasts_filtering");
    private static final Component CREDITS = Components.of("gui.credits");
    private static final Component GITHUB_LABEL = Components.of("label.creator_note");
    private static final Component DISCORD_TOOLTIP = Components.of("tooltip.discord");
    private static final Component BOOSTY_TOOLTIP = Components.of("tooltip.boosty");
    private static final Component YOUTUBE_TOOLTIP = Components.of("tooltip.youtube");

    private static final URI GITHUB_URI = URI.create("https://github.com/Bivrik");
    private static final URI DISCORD_URI = URI.create("https://discord.gg/9XuRDgbbZe");
    private static final URI BOOSTY_URI = URI.create("https://boosty.to/bivrik");
    private static final URI YOUTUBE_URI = URI.create("https://www.youtube.com/@modsEnjoyer");

    private final String splash;

    private Button backButton;
    private Button toastConfigButton;
    private Button generalConfigButton;
    private Button toastsFilteringButton;
    private Button creditsButton;
    private PlainTextButton supportButton;

    public FancyToastsScreen(Screen parent) {
        super(TITLE, parent);

        this.splash = FancyToasts.getInstance().getSplashManager().getSplash();
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2 - HALF_TITLE_BUTTON_WIDTH;
        int yCenter = this.height / 2;

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

        List<ImageButton> linkButtons = new ArrayList<>();

        linkButtons.add(createLinkButton("links/discord", "links/discord_hover",
                DISCORD_URI, DISCORD_TOOLTIP));

        linkButtons.add(createLinkButton("links/boosty", "links/boosty_hover",
                BOOSTY_URI, BOOSTY_TOOLTIP));

        linkButtons.add(createLinkButton("links/youtube", "links/youtube_hover",
                YOUTUBE_URI, YOUTUBE_TOOLTIP));

        int x = toastConfigButton.getRight() + PADDING;
        int y = toastConfigButton.getY();
        for (ImageButton button : linkButtons) {
            button.setPosition(x, y);
            addFWidget(button);
            y += button.getHeight() + 5;
        }

        int supportButtonWidth = this.font.width(GITHUB_LABEL);
        Button.OnPress openGithubAction = ConfirmLinkScreen.confirmLink(this, GITHUB_URI);
        supportButton = new PlainTextButton(this.width - supportButtonWidth - 2, this.height - 9 - 1, supportButtonWidth, 9, GITHUB_LABEL, openGithubAction, this.font);
        addFWidget(supportButton);
    }

    @Override
    protected Button createButton(Component label, Button.OnPress action, int x, int y) {
        return super.createButton(label, action, x, y, TITLE_BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private ImageButton createLinkButton(String icon, String iconHovered, URI link, Component tooltip) {
        Button.OnPress action = ConfirmLinkScreen.confirmLink(this, link);
        ImageButton button = new ImageButton(0, 0, 18, 18, new WidgetSprites(ResourceLocations.of(icon), ResourceLocations.of(iconHovered)), action);
        button.setTooltip(Tooltip.create(tooltip));
        return button;
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
        guiGraphics.drawCenteredString(this.font, splash, this.width / 2, 12 + 9, Color.YELLOW.getARGB());
        context.pop();
    }
}
