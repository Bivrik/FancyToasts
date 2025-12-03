package net.bivrik.fancytoasts.platform.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiContext {
    private final GuiGraphics guiGraphics;
    private final PoseStack stack;

    public GuiContext(GuiGraphics guiGraphics) {
        this.guiGraphics = guiGraphics;
        this.stack = guiGraphics.pose();
    }

    public PoseStack stack() {
        return stack;
    }

    public GuiGraphics guiGraphics() {
        return guiGraphics;
    }

    public void push() {
        stack.pushPose();
    }

    public void pop() {
        stack.popPose();
    }

    public void translate(float x, float y, float z) {
        stack.translate(x, y, z);
    }

    public void translate(float x, float y) {
        stack.translate(x, y, 0);
    }

    public void rotateAround(float rotation, float ox, float oy) {
        stack.rotateAround(Axis.ZP.rotation(rotation), ox, oy, 0);
    }

    public void scaleAround(float sx, float sy, float ox, float oy) {
        stack.translate(ox, oy, 0);
        stack.scale(sx, sy, 1);
        stack.translate(-ox, -oy, 0);
    }

    public void scaleAround(float scale, float ox, float oy) {
        scaleAround(scale, scale, ox, oy);
    }

    public void drawGUITexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight) {
        guiGraphics.blit(textureLocation, x, y, uv.u(), uv.v(), width, height, textureWidth, textureHeight);
    }

    public void drawGUITexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight, int color) {
        if (color == Colors.WHITE) {
            drawGUITexture(textureLocation, x, y, width, height, uv, textureWidth, textureHeight);
            return;
        }

        float alpha = ((color >> 24) & 0xFF) / 255.0f;

        RenderSystem.enableBlend();
        guiGraphics.setColor(1, 1, 1, alpha);
        guiGraphics.blit(textureLocation, x, y, uv.u(), uv.v(), width, height, textureWidth, textureHeight);
        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    public void drawGUITexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int color) {
        drawGUITexture(textureLocation, x, y, width, height, uv, 256, 256, color);
    }

    public void drawSprite(ResourceLocation spriteLocation, int x, int y, int width, int height) {
        guiGraphics.blit(spriteLocation, x, y, 0, 0, width, height, width, height);
    }

    public void fill(int x, int y, int width, int height, int color) {
        guiGraphics.fill(x, y, x + width, y + height, color);
    }
}
