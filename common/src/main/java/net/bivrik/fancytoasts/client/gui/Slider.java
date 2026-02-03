package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.core.Debug;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;
import java.util.function.Function;

public class Slider extends AbstractSliderButton {
    private final Component label;
    private final double min;
    private final double max;
    private final float threshold;

    private Function<Float, Component> displayer;
    private Consumer<Float> responder;

    public Slider(int x, int y, int width, int height, Component label, float value, float min, float max, float threshold) {
        super(x, y, width, height, Component.empty(), (value - min) / (max - min));
        this.label = label;
        this.min = min;
        this.max = max;
        this.threshold = threshold;

        this.updateMessage();
    }

    public Slider(int x, int y, int width, int height, Component label, float value, float max, float threshold) {
        this(x, y, width, height, label, value, 0.0f, max, threshold);
    }

    public Slider(int x, int y, int width, int height, Component label, float value, float max) {
        this(x, y, width, height, label, value, 0.0f, max, 0.0f);
    }

    public Slider setResponder(Consumer<Float> responder) {
        this.responder = responder;
        return this;
    }

    public Slider setDisplayer(Function<Float, Component> displayer) {
        this.displayer = displayer;
        this.updateMessage();
        return this;
    }

    private float getValue() {
        float value = (float) Math.round(Mth.lerp(this.value, min, max) * 100) / 100;
        int intValue = Math.round(value);

        if (threshold != 0.0f) {
            if (value >= intValue - threshold && value <= intValue + threshold) {
                return intValue;
            }
        }

        return value;
    }

    @Override
    protected void updateMessage() {
        if (displayer == null) displayer = value -> Component.literal(value.toString());

        this.setMessage(CommonComponents.optionNameValue(label, displayer.apply(getValue())));
    }

    @Override
    protected void applyValue() {
        if (responder == null) return;

        responder.accept(getValue());
    }
}
