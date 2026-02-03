package net.bivrik.fancytoasts.client.gui.screen;

import net.bivrik.fancytoasts.client.gui.LayoutValues;
import net.bivrik.fancytoasts.client.gui.Slider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class UniversalScreen extends Screen {
    private final List<Renderable> renderables = new ArrayList<>();
    protected final Screen parent;

    public UniversalScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void onClose() {
        toParentScreen();
    }

    protected <T extends GuiEventListener & Renderable & NarratableEntry> T addFWidget(T widget) {
        addFRenderable(widget);
        return super.addWidget(widget);
    }

    protected <T extends GuiEventListener & Renderable & NarratableEntry> void removeFWidget(T widget) {
        removeFRenderable(widget);
        super.removeWidget(widget);
    }

    protected <T extends Renderable> T addFRenderable(T renderable) {
        renderables.add(renderable);
        return renderable;
    }

    protected <T extends Renderable> void removeFRenderable(T renderable) {
        renderables.remove(renderable);
    }

    @Override
    protected void clearWidgets() {
        renderables.clear();
        super.clearWidgets();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        drawRenderables(guiGraphics, mouseX, mouseY, partialTick);
        drawTitle(guiGraphics);
    }

    public void drawRenderables(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (Renderable renderable : renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    protected void toParentScreen() {
        openScreen(parent);
    }

    protected void openScreen(Screen screen) {
        Objects.requireNonNull(this.minecraft).setScreen(screen);
    }

    protected void drawTitle(GuiGraphics guiGraphics) {
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 12, -1);
    }

    protected Button createButton(Component label, Button.OnPress action, int x, int y, int width, int height, Tooltip tooltip) {
        var button = Button.builder(label, action);

        if (tooltip != null) {
            button.tooltip(tooltip);
        }

        return button.bounds(x, y, width, height).build();
    }

    protected Button createButton(Component label, Button.OnPress action, int x, int y, int width, int height) {
        return createButton(label, action, x, y, width, height, null);
    }

    protected Button createButton(Component label, Button.OnPress action, int x, int y, Tooltip tooltip) {
        return createButton(label, action, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, tooltip);
    }

    protected Button createButton(Component label, Button.OnPress action, int x, int y) {
        return createButton(label, action, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, null);
    }

    protected Slider createSlider(Component label, float initialValue, float minValue, float maxValue, float threshold, Function<Float, Component> displayer, Consumer<Float> responder, int x, int y, int width, int height, Tooltip tooltip) {
        var slider = new Slider(x, y, width, height, label, initialValue, minValue, maxValue, threshold).setDisplayer(displayer).setResponder(responder);

        if (tooltip != null) {
            slider.setTooltip(tooltip);
        }

        return slider;
    }

    protected Slider createSlider(Component label, float initialValue, float maxValue, float threshold, Function<Float, Component> displayer, Consumer<Float> responder, int x, int y, int width, int height, Tooltip tooltip) {
        return createSlider(label, initialValue, 0.0f, maxValue, threshold, displayer, responder, x, y, width, height, tooltip);
    }

    protected Slider createSlider(Component label, float initialValue, float maxValue, float threshold, Function<Float, Component> displayer, Consumer<Float> responder, int x, int y, Tooltip tooltip) {
        return createSlider(label, initialValue, maxValue, threshold, displayer, responder, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, tooltip);
    }

    protected Slider createSlider(Component label, float initialValue, float maxValue, Function<Float, Component> displayer, Consumer<Float> responder, int x, int y, Tooltip tooltip) {
        return createSlider(label, initialValue, maxValue, 0.0f, displayer, responder, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, tooltip);
    }

    protected Slider createSlider(Component label, float initialValue, float maxValue, float threshold, Function<Float, Component> displayer, Consumer<Float> responder, int x, int y) {
        return createSlider(label, initialValue, maxValue, threshold, displayer, responder, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, null);
    }

    protected Slider createSlider(Component label, float initialValue, float maxValue, Function<Float, Component> displayer, Consumer<Float> responder, int x, int y) {
        return createSlider(label, initialValue, maxValue, 0.0f, displayer, responder, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, null);
    }

    protected CycleButton<Boolean> createBooleanButton(Component label, boolean initialValue, CycleButton.OnValueChange<Boolean> action, int x, int y, int width, int height, Tooltip tooltip) {
        var onOffButton = CycleButton.onOffBuilder().withInitialValue(initialValue);

        if (tooltip != null) {
            onOffButton.withTooltip(value -> tooltip);
        }

        return onOffButton.create(x, y, width, height, label, action);
    }

    protected CycleButton<Boolean> createBooleanButton(Component label, boolean initialValue, CycleButton.OnValueChange<Boolean> action, int x, int y, Tooltip tooltip) {
        return createBooleanButton(label, initialValue, action, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, tooltip);
    }

    protected CycleButton<Boolean> createBooleanButton(Component label, boolean initialValue, CycleButton.OnValueChange<Boolean> action, int x, int y) {
        return createBooleanButton(label, initialValue, action, x, y, LayoutValues.BUTTON_WIDTH, LayoutValues.BUTTON_HEIGHT, null);
    }
}
