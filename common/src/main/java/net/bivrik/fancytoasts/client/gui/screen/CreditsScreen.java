package net.bivrik.fancytoasts.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancytoasts.core.manager.CreditsManager;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.client.gui.CreditsList;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class CreditsScreen extends UniversalScreen {
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocations.fromMinecraft("textures/misc/vignette.png");

    private final CreditsManager.CreditsData creditsData;

    private CreditsList creditsList;

    public CreditsScreen(Screen parent) {
        super(Component.empty(), parent);
        this.creditsData = Managers.getCreditsManager().getCredits();
    }

    @Override
    protected void init() {
        creditsList = this.addFRenderable(new CreditsList(this.minecraft, this.width , this.height, 0, 0, creditsData));
        this.setFocused(creditsList);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.drawRenderables(guiGraphics, mouseX, mouseY, partialTick);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        new GuiContext(guiGraphics).drawGUITexture(VIGNETTE_LOCATION, 0, 0, this.width, this.height, TextureUV.ZERO, this.width, this.height);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        creditsList.scroll();
    }
}
