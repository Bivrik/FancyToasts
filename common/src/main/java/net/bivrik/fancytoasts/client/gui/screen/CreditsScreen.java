package net.bivrik.fancytoasts.client.gui.screen;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.client.credits.CreditsList;
import net.bivrik.fancytoasts.client.credits.CreditsManager;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.Identifiers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class CreditsScreen extends UniversalScreen {
    private static final Identifier VIGNETTE_LOCATION = Identifiers.fromMinecraft("textures/misc/credits_vignette.png");
    private static final Component TITLE = Components.of("gui.credits");

    private final CreditsManager.CreditsData creditsData;

    private CreditsList creditsList;

    public CreditsScreen(Screen parent) {
        super(TITLE, parent);

        this.creditsData = FancyToasts.getInstance().getCreditsManager().getCredits();
    }

    @Override
    protected void init() {
        int offset = 12 + 9 + 12;
        creditsList = new CreditsList(this.minecraft, this.width, this.height - offset * 2, 8, offset, creditsData);
        addRenderableOnly(creditsList);
        setFocused(creditsList);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int offset = 12 + 9 + 12;
        int width = this.width;
        int height = this.height - offset * 2;
        graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, offset, 0, 0, width, height, width, height);
        drawTitle(graphics);

        creditsList.scroll();
    }
}
