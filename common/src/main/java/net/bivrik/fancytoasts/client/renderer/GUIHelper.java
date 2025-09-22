package net.bivrik.fancytoasts.client.renderer;

import net.bivrik.fancytoasts.utility.Colors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3x2fStack;

public class GUIHelper {
    public static void translate(Matrix3x2fStack matrix, float x, float y) {
        matrix.translate(x, y);
    }

    public static void scale(Matrix3x2fStack matrix, float x, float y) {
        matrix.scale(x, y);
    }
    public static void scale(Matrix3x2fStack matrix, float scale) {
        matrix.scale(scale);
    }

    public static void rotate(Matrix3x2fStack matrix, float rotation) {
        matrix.rotate(rotation);
    }

    public static Matrix3x2fStack get(GuiGraphics gui) {
        return gui.pose();
    }

    public static void push(Matrix3x2fStack matrix) {
        matrix.pushMatrix();
    }

    public static void pop(Matrix3x2fStack matrix) {
        matrix.popMatrix();
    }

    public static void drawGUITexture(GuiGraphics graphics, ResourceLocation atlas, int x, int y, int u, int v, int width, int height, int color) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, atlas, x, y, u, v, width, height, 256, 256, color);
    }

    public static void drawGUITexture(GuiGraphics graphics, ResourceLocation atlas, int x, int y, int u, int v, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, atlas, x, y, u, v, width, height, 256, 256, -1);
    }
}
