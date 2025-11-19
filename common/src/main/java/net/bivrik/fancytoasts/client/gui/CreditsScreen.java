package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.client.ui.CreditsList;
import net.bivrik.fancytoasts.platform.Managers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.bivrik.fancytoasts.client.ui.LayoutValues.*;

public class CreditsScreen extends UniversalScreen {
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/credits_vignette.png");
    private CreditsList creditsList;

    protected CreditsScreen(Screen parent) {
        super(Component.empty(), parent);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.blit(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
        creditsList.scroll();
    }

    @Override
    protected void init() {
        var creditsData = Managers.creditsManager().getCredits();
        creditsList = this.addRenderableWidget(new CreditsList(this.minecraft, this.width , this.height, PADDING, 0, creditsData));

        this.setFocused(creditsList);
    }
}
