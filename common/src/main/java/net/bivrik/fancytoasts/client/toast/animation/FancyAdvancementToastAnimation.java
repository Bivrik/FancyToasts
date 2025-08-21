package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.renderer.GUIHelper;
import net.bivrik.fancytoasts.client.toast.texture.TextureUV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class FancyAdvancementToastAnimation {
    private FancyAdvancementSetup setup;

    public void setup(FancyAdvancementSetup setup) {
        this.setup = setup;
    }
    protected FancyAdvancementSetup getSetup() {
        return setup;
    }

    public abstract void draw(GuiGraphics graphics, Minecraft minecraft, FancyAdvancementToast toast, long time);

    public abstract int getDuration();

    public abstract int getToastSoundTiming();

    protected void drawIcon(GuiGraphics graphics) {
        TextureUV frameUV = setup.uvs().frame();
        GUIHelper.drawGUITexture(graphics, setup.texture(), 68, 0, frameUV.u(), frameUV.v(), 26, 26);

        graphics.renderFakeItem(setup.display().getIcon(), 73, 5);
    }

    protected void drawBanner(GuiGraphics graphics) {
        TextureUV bannerUV = setup.uvs().banner();
        GUIHelper.drawGUITexture(graphics, setup.texture(), 0, 5, bannerUV.u(), bannerUV.v(), 162, 14);
    }

    protected void drawBackground(GuiGraphics graphics) {
        GUIHelper.drawGUITexture(graphics, setup.texture(), 0, 20, 0, 0, 162, 40);
        GUIHelper.drawGUITexture(graphics, setup.texture(), 144, 56, 0, 108, 9, 14);
    }
}
