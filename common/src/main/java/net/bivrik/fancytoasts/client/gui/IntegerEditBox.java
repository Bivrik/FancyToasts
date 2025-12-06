package net.bivrik.fancytoasts.client.gui;

import com.google.common.primitives.Ints;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class IntegerEditBox extends EditBox {
    public IntegerEditBox(Font font, int x, int y, int width, int height, EditBox editBox, Component message, int initialValue) {
        super(font, x, y, width, height, editBox, message);

        setValue(String.valueOf(initialValue));
    }

    @Override
    public void insertText(@NotNull String textToWrite) {
        super.insertText(textToWrite);
    }

    public void setIntegerResponder(Consumer<Integer> consumer) {
        Integer value = Ints.tryParse(this.getValue());
        if (value != null) {
            consumer.accept(value);
        }
    }

    public void setIntegerValue(int value) {
        setValue(String.valueOf(value));
    }
}
