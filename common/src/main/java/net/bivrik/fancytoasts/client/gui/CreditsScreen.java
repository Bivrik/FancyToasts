package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.client.toast.TextureUV;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.client.ui.CreditsList;
import net.bivrik.fancytoasts.platform.Managers;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.bivrik.fancytoasts.client.ui.LayoutValues.*;

public class CreditsScreen extends UniversalScreen {
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocations.fromMinecraft("textures/misc/credits_vignette.png");
    private CreditsList creditsList;

    protected CreditsScreen(Screen parent) {
        super(Component.empty(), parent);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        new GuiContext(guiGraphics).drawTexture(VIGNETTE_LOCATION, 0, 0, this.width, this.height, TextureUV.ZERO);
        creditsList.scroll();
    }

    @Override
    protected void init() {
        var creditsData = Managers.creditsManager().getCredits();
        creditsList = this.addRenderableWidget(new CreditsList(this.minecraft, this.width , this.height, PADDING, 0, creditsData));

        this.setFocused(creditsList);
    }
}
