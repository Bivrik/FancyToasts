package net.bivrik.fancytoasts.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.BUTTON_HEIGHT;
import static net.bivrik.fancytoasts.client.gui.LayoutValues.BUTTON_WIDTH;

public class GeneralConfigScreen extends Screen {
    private final Screen parent;

    private Button backButton;

    public GeneralConfigScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> toParentScreen())
                .bounds(this.width / 2 - BUTTON_WIDTH / 2, this.height - BUTTON_HEIGHT - 6, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    private void toParentScreen() {
        Objects.requireNonNull(this.minecraft).setScreen(parent);
    }

    @Override
    public void onClose() {
        toParentScreen();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, -1);
    }
}
