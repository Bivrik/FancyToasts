package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.client.toast.TextureUV;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3x2fStack;

public class GuiContext {
    private final GuiGraphics guiGraphics;
    private final Matrix3x2fStack stack;

    public GuiContext(GuiGraphics guiGraphics) {
        this.guiGraphics = guiGraphics;
        this.stack = guiGraphics.pose();
    }

    public Matrix3x2fStack stack() {
        return stack;
    }

    public GuiGraphics guiGraphics() {
        return guiGraphics;
    }

    public void push() {
        stack.pushMatrix();
    }

    public void pop() {
        stack.popMatrix();
    }

    public void translate(float x, float y) {
        stack.translate(x, y);
    }

    public void rotateAround(float rotation, float ox, float oy) {
        stack.rotateAbout(rotation, ox, oy);
    }

    public void scaleAround(float sx, float sy, float ox, float oy) {
        stack.scaleAround(sx, sy, ox, oy);
    }

    public void scaleAround(float scale, float ox, float oy) {
        scaleAround(scale, scale, ox, oy);
    }

    public void drawTexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight, int color) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, textureLocation, x, y, uv.u(), uv.v(), width, height, textureWidth, textureHeight, color);
    }

    public void drawTexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight) {
        drawTexture(textureLocation, x, y, width, height, uv, textureWidth, textureHeight, Colors.WHITE);
    }

    public void drawTexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int color) {
        drawTexture(textureLocation, x, y, width, height, uv, 256, 256, color);
    }

    public void drawTexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv) {
        drawTexture(textureLocation, x, y, width, height, uv, 256, 256, Colors.WHITE);
    }

    public void drawSprite(ResourceLocation spriteLocation, int x, int y, int width, int height, int color) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, spriteLocation, x, y, width, height, color);
    }

    public void drawSprite(ResourceLocation spriteLocation, int x, int y, int width, int height) {
        drawSprite(spriteLocation, x, y, width, height, Colors.WHITE);
    }

    public void fill(int x, int y, int width, int height, int color) {
        guiGraphics.fill(x, y, x + width, y + height, color);
    }
}
