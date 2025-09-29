package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.client.toast.texture.TextureUV;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class FancyAdvancementToastAnimation {
    protected FancyAdvancementSetup setup;
    protected FancyAdvancementToast toast;
    private int guiColor = -1;

    public void setup(FancyAdvancementSetup setup, FancyAdvancementToast toast) {
        this.setup = setup;
        this.toast = toast;
    }

    public void draw(GuiGraphics graphics, Minecraft minecraft, long time) {
        if (minecraft.screen != null && Common.getConfigManager().getGeneralConfig().getScreenBehavior() == AdvancementToastScreenBehavior.TRANSPARENT) {
            this.guiColor = Colors.alpha(126, -1);
        }
        else {
            this.guiColor = -1;
        }
    }

    public abstract int getDuration();

    public abstract int getToastSoundTiming();

    protected void drawIcon(GuiGraphics graphics) {
        TextureUV frameUV = setup.uvs().frame();
        GUIs.drawTexture(graphics, setup.texture(), 68, 0, frameUV.u(), frameUV.v(), 26, 26, guiColor);

        graphics.renderFakeItem(setup.display().getIcon(), 73, 5);
    }

    protected void drawBanner(GuiGraphics graphics) {
        TextureUV bannerUV = setup.uvs().banner();
        GUIs.drawTexture(graphics, setup.texture(), 0, 5, bannerUV.u(), bannerUV.v(), 162, 14, guiColor);
    }

    protected void drawBackground(GuiGraphics graphics) {
        GUIs.drawTexture(graphics, setup.texture(), 0, 20, 0, 0, 162, 40, guiColor);
        GUIs.drawTexture(graphics, setup.texture(), 144, 56, 0, 108, 9, 14, guiColor);
    }
}
