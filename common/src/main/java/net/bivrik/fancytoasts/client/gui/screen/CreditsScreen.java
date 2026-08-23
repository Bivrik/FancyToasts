package net.bivrik.fancytoasts.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.client.credits.CreditsList;
import net.bivrik.fancytoasts.client.credits.CreditsManager;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CreditsScreen extends UniversalScreen {
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocations.fromMinecraft("textures/misc/credits_vignette.png");
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
        addFRenderable(creditsList);
        setFocused(creditsList);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int offset = 12 + 9 + 12;
        int width = this.width;
        int height = this.height - offset * 2;
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        guiGraphics.blit(VIGNETTE_LOCATION, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        guiGraphics.blit(VIGNETTE_LOCATION, 0, offset, 0, 0, width, height, width, height);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        creditsList.scroll();

        drawRenderables(guiGraphics, mouseX, mouseY, partialTick);
        drawTitle(guiGraphics);
    }
}
