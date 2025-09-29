package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3x2fStack;

public class GUIs {
    public static void translate(Matrix3x2fStack stack, float x, float y) {
        stack.translate(x, y);
    }

    public static void scale(Matrix3x2fStack stack, float x, float y) {
        stack.scale(x, y);
    }
    public static void scale(Matrix3x2fStack stack, float scale) {
        stack.scale(scale);
    }

    public static void rotate(Matrix3x2fStack stack, float rotation) {
        stack.rotate(rotation);
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
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, 256, 256, -1);
    }
}
