package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.client.toast.texture.TextureUV;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;

public abstract class FancyAdvancementToastAnimation {
    protected FancyAdvancementSetup setup;
    protected FancyAdvancementToast toast;
    private int guiColor = -1;
    private float guiAlpha;

    public void setup(FancyAdvancementSetup setup, FancyAdvancementToast toast) {
        this.setup = setup;
        this.toast = toast;
    }

    public void draw(GuiGraphics guiGraphics, Minecraft minecraft, long time) {
        if (Common.getAdvancementToastManager().isScreenOpened()
                && Common.getConfigManager().getGeneralConfig().getScreenBehavior() == AdvancementToastScreenBehavior.TRANSPARENT) {
            guiAlpha = 0.5F;
        }
        else {
            guiAlpha = 1.0F;
        }

        guiColor = Colors.alpha((int) (guiAlpha * 255), Colors.WHITE);
    }

    public abstract int getDuration();

    public abstract int getToastSoundTiming();

    protected void drawIcon(GuiGraphics graphics, float alpha) {
        TextureUV frameUV = setup.uvs().frame();
        GUIs.drawTexture(graphics, setup.texture(), 68, 0, frameUV.u(), frameUV.v(), 26, 26, Colors.alpha((int) (alpha * guiAlpha * 255), guiColor));

        graphics.renderFakeItem(setup.display().getIcon(), 73, 5);
    }
    protected void drawIcon(GuiGraphics guiGraphics) {
        drawIcon(guiGraphics, 1.0F);
    }

    protected void drawBanner(GuiGraphics graphics, float alpha) {
        TextureUV bannerUV = setup.uvs().banner();
        GUIs.drawTexture(graphics, setup.texture(), 0, 5, bannerUV.u(), bannerUV.v(), 162, 14, Colors.alpha((int) (alpha * guiAlpha * 255), guiColor));
    }
    protected void drawBanner(GuiGraphics graphics) {
        drawBanner(graphics, 1.0F);
    }

    protected void drawBackground(GuiGraphics graphics, float alpha) {
        GUIs.drawTexture(graphics, setup.texture(), 0, 20, 0, 0, 162, 40, Colors.alpha((int) (alpha * guiAlpha * 255), guiColor));
        GUIs.drawTexture(graphics, setup.texture(), 144, 56, 0, 108, 9, 14, Colors.alpha((int) (alpha * guiAlpha * 255), guiColor));
    }
    protected void drawBackground(GuiGraphics graphics) {
        drawBackground(graphics, 1.0F);
    }
}
