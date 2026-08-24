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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class FancyToastsScreen extends UniversalScreen {
    private static final int TITLE_BUTTON_WIDTH = 200;
    private static final int HALF_TITLE_BUTTON_WIDTH = TITLE_BUTTON_WIDTH / 2;
    private static final ResourceLocation LINKS = ResourceLocations.of("textures/gui/links.png");

    private static final Component TITLE = Component.literal("Fancy Toasts");
    private static final Component TOAST_SETTINGS = Components.of("gui.toast_settings");
    private static final Component GENERAL_SETTINGS = Components.of("gui.general_settings");
    private static final Component TOASTS_FILTERING = Components.of("gui.toasts_filtering");
    private static final Component CREDITS = Components.of("gui.credits");
    private static final Component GITHUB_LABEL = Components.of("label.creator_note");
    private static final Component DISCORD_TOOLTIP = Components.of("tooltip.discord");
    private static final Component BOOSTY_TOOLTIP = Components.of("tooltip.boosty");
    private static final Component YOUTUBE_TOOLTIP = Components.of("tooltip.youtube");

    private static final String GITHUB_LINK = "https://github.com/Bivrik";
    private static final String DISCORD_LINK = "https://discord.gg/9XuRDgbbZe";
    private static final String BOOSTY_LINK = "https://boosty.to/bivrik";
    private static final String YOUTUBE_LINK = "https://www.youtube.com/@modsEnjoyer";

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

        List<LinkButton> linkButtons = new ArrayList<>();

        linkButtons.add(createLinkButton(DISCORD_LINK, DISCORD_TOOLTIP, new UV(0, 18), new UV(18, 18)));

        linkButtons.add(createLinkButton(BOOSTY_LINK, BOOSTY_TOOLTIP, new UV(0, 0), new UV(18, 0)));

        linkButtons.add(createLinkButton(YOUTUBE_LINK, YOUTUBE_TOOLTIP, new UV(0, 36), new UV(18, 36)));

        int x = toastConfigButton.getX() + toastConfigButton.getWidth() + PADDING;
        int y = toastConfigButton.getY();
        for (LinkButton button : linkButtons) {
            button.setPosition(x, y);
            addFWidget(button);
            y += button.getHeight() + 5;
        }

        int supportButtonWidth = this.font.width(GITHUB_LABEL);
        Button.OnPress openGithubAction = ConfirmLinkScreen.confirmLink(GITHUB_LINK, this, true);
        supportButton = new PlainTextButton(this.width - supportButtonWidth - 2, this.height - 9 - 1, supportButtonWidth, 9, GITHUB_LABEL, openGithubAction, this.font);
        addFWidget(supportButton);
    }

    @Override
    protected Button createButton(Component label, Button.OnPress action, int x, int y) {
        return super.createButton(label, action, x, y, TITLE_BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private LinkButton createLinkButton(String link, Component tooltip, UV iconUv, UV iconHoveredUv) {
        Button.OnPress action = ConfirmLinkScreen.confirmLink(link, this, true);
        LinkButton button = new LinkButton(LINKS, action, iconUv, iconHoveredUv);
        button.setTooltip(Tooltip.create(tooltip));
        return button;
    }

    private static class LinkButton extends Button {
        private final ResourceLocation sprites;
        private final UV iconUv;
        private final UV iconHoveredUv;

        public LinkButton(ResourceLocation sprites, OnPress onPress, UV iconUv, UV iconHoveredUv) {
            super(0, 0, 18, 18, Component.empty(), onPress, DEFAULT_NARRATION);

            this.sprites = sprites;
            this.iconUv = iconUv;
            this.iconHoveredUv = iconHoveredUv;
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            UV uv;
            if (isHoveredOrFocused()) {
                uv = iconHoveredUv;
            } else {
                uv = iconUv;
            }

            guiGraphics.blit(sprites, getX(), getY(), uv.uOffset, uv.vOffset, getWidth(), getHeight(), 64, 64);
        }
    }

    private record UV(int uOffset, int vOffset) {}

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
