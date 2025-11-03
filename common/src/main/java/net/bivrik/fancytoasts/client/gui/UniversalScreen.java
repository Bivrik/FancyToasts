package net.bivrik.fancytoasts.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.bivrik.fancytoasts.client.ui.LayoutValues.BUTTON_HEIGHT;
import static net.bivrik.fancytoasts.client.ui.LayoutValues.BUTTON_WIDTH;

public abstract class UniversalScreen extends Screen {
    protected final Screen parent;

    protected UniversalScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void onClose() {
        toParentScreen();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.drawTitle(guiGraphics);
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

    protected Button createButton(Component title, Button.OnPress action, int x, int y, int width, int height, Component tooltip) {
        return this.addRenderableWidget(Button.builder(title, action).bounds(x, y, width, height).tooltip(Tooltip.create(tooltip)).build());
    }

    protected Button createButton(Component title, Button.OnPress action, int x, int y, Component tooltip) {
        return createButton(title, action, x, y, BUTTON_WIDTH, BUTTON_HEIGHT, tooltip);
    }

    protected Button createButton(Component title, Button.OnPress action, int x, int y, int width, int height) {
        return this.addRenderableWidget(Button.builder(title, action).bounds(x, y, width, height).build());
    }

    protected Button createButton(Component title, Button.OnPress action, int x, int y) {
        return createButton(title, action, x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }


}
