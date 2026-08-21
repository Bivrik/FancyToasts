package net.bivrik.fancytoasts.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancytoasts.client.gui.CreditsList;
import net.bivrik.fancytoasts.core.manager.CreditsManager;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.PADDING;

public class CreditsScreen extends UniversalScreen {
    private static final Identifier VIGNETTE_LOCATION = Identifiers.fromMinecraft("textures/misc/credits_vignette.png");

    private final CreditsManager.CreditsData creditsData;

    private CreditsList creditsList;

    public CreditsScreen(Screen parent) {
        super(Component.empty(), parent);

        this.creditsData = FancyToasts.getInstance().getCreditsManager().getCredits();
    }

    @Override
    protected void init() {
        creditsList = this.addFRenderable(new CreditsList(this.minecraft, this.width , this.height, PADDING, 0, creditsData));
        this.setFocused(creditsList);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphicsExtractor, mouseX, mouseY, partialTick);

        new GuiContext(guiGraphicsExtractor).drawTexture(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, 0, this.width, this.height, TextureUV.ZERO, this.width, this.height);

        creditsList.scroll();
    }
}
