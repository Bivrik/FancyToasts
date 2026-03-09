package net.bivrik.fancytoasts.platform.utility;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3x2fStack;

public class GuiContext {
    private static final int WHITE = -1;

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

    public void drawTexture(RenderPipeline pipeline, ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight, int color) {
        guiGraphics.blit(pipeline, textureLocation, x, y, uv.u(), uv.v(), width, height, textureWidth, textureHeight, color);
    }

    public void drawTexture(RenderPipeline pipeline, ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight) {
        drawTexture(pipeline, textureLocation, x, y, width, height, uv, textureWidth, textureHeight, WHITE);
    }

    public void drawTexture(RenderPipeline pipeline, ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv) {
        drawTexture(pipeline, textureLocation, x, y, width, height, uv, width, height, WHITE);
    }

    public void drawGUITexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight, int color) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, textureLocation, x, y, uv.u(), uv.v(), width, height, textureWidth, textureHeight, color);
    }

    public void drawGUITexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int textureWidth, int textureHeight) {
        drawGUITexture(textureLocation, x, y, width, height, uv, textureWidth, textureHeight, WHITE);
    }

    public void drawGUITexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv, int color) {
        drawGUITexture(textureLocation, x, y, width, height, uv, 256, 256, color);
    }

    public void drawGUITexture(ResourceLocation textureLocation, int x, int y, int width, int height, TextureUV uv) {
        drawGUITexture(textureLocation, x, y, width, height, uv, 256, 256, WHITE);
    }

    public void drawSprite(ResourceLocation spriteLocation, int x, int y, int width, int height, int color) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, spriteLocation, x, y, width, height, color);
    }

    public void drawSprite(ResourceLocation spriteLocation, int x, int y, int width, int height) {
        drawSprite(spriteLocation, x, y, width, height, WHITE);
    }

    public void fill(int x, int y, int width, int height, int color) {
        guiGraphics.fill(x, y, x + width, y + height, color);
    }
}
