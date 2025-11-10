package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3x2fStack;

public class GUIs {
    public static void translate(Matrix3x2fStack stack, float x, float y) {
        stack.translate(x, y);
    }

    public static void scaleAround(Matrix3x2fStack stack, float sx, float sy, float ox, float oy) {
        stack.scaleAround(sx, sy, ox, oy);
    }
    public static void scaleAround(Matrix3x2fStack stack, float scale, float ox, float oy) {
        stack.scaleAround(scale, ox, oy);
    }

    public static void rotateAround(Matrix3x2fStack stack, float rotation, float ox, float oy) {
        stack.rotateAbout(rotation, ox, oy);
    }

    public static Matrix3x2fStack getStack(GuiGraphics guiGraphics) {
        return guiGraphics.pose();
    }

    public static void push(Matrix3x2fStack stack) {
        stack.pushMatrix();
    }

    public static void pop(Matrix3x2fStack stack) {
        stack.popMatrix();
    }

    public static void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height, int color) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, 256, 256, color);
    }

    public static void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, width, height, -1);
    }
}
