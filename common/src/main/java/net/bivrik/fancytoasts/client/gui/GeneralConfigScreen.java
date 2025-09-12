package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.GeneralConfigData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;
import static net.bivrik.fancytoasts.client.gui.LayoutValues.PADDING;

public class GeneralConfigScreen extends Screen {
    private final Screen parent;
    private final GeneralConfigData generalConfigData;

    private Button doneButton;
    private Button backButton;
    private CycleButton<Boolean> jadeCompatCycleButton;
    private CycleButton<Boolean> soundsEnabledCycleButton;
    private VolumeSlider taskVolumeSlider;
    private VolumeSlider goalVolumeSlider;
    private VolumeSlider challengeVolumeSlider;

    public GeneralConfigScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
        this.generalConfigData = Common.getConfigManager().getGeneralConfig();
    }

    @Override
    protected void init() {
        doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> done())
                .bounds(this.width / 2 + 100 - 125 + PADDING / 2, this.height - 26, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> toParentScreen())
                .bounds(this.width / 2 - 125 - PADDING / 2, this.height - 20 - 6, 100, BUTTON_HEIGHT).build());

        jadeCompatCycleButton = this.addRenderableWidget(CycleButton.onOffBuilder()
                .withInitialValue(generalConfigData.isJadeCompatEnabled())
                .withTooltip((value) -> Tooltip.create(Component.literal("Toggles jade compatibility. If toggled, then Jade plaque will be hidden when advancement toast is appeared")))
                .create(this.width / 2 - BUTTON_WIDTH / 2, this.height / 2, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Jade Compatibility"), (button, value) -> generalConfigData.setJadeCompatEnabled(value)));

        soundsEnabledCycleButton = this.addRenderableWidget(CycleButton.onOffBuilder()
                .withInitialValue(generalConfigData.areSoundsEnabled())
                .withTooltip((value) -> Tooltip.create(Component.literal("Toggles sounds. All sounds can be muted")))
                .create(this.width / 2 - BUTTON_WIDTH / 2, this.height / 2 + 20 + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Toast Sounds"), (button, value) -> generalConfigData.setSoundsEnabled(value)));

        taskVolumeSlider = this.addRenderableWidget(new VolumeSlider(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Task Volume"), generalConfigData.getTaskVolume()));
        taskVolumeSlider.setResponder(generalConfigData::setTaskVolume);

        goalVolumeSlider = this.addRenderableWidget(new VolumeSlider(0, 20 + PADDING, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Goal Volume"), generalConfigData.getGoalVolume()));
        goalVolumeSlider.setResponder(generalConfigData::setGoalVolume);

        challengeVolumeSlider = this.addRenderableWidget(new VolumeSlider(0, 40 + PADDING * 2, BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal("Challenge Volume"), generalConfigData.getChallengeVolume()));
        challengeVolumeSlider.setResponder(generalConfigData::setChallengeVolume);
    }

    private void done() {
        ConfigHandler.save(generalConfigData);
        toParentScreen();
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

    private static class VolumeSlider extends AbstractSliderButton {
        private Consumer<Float> responder;
        private Component message;

        public VolumeSlider(int x, int y, int width, int height, Component message, double value) {
            super(x, y, width, height, Component.empty(), value / 2);
            this.message = message;

            this.updateMessage();
        }

        public void setResponder(Consumer<Float> responder) {
            this.responder = responder;
        }

        @Override
        protected void updateMessage() {
            this.setMessage(CommonComponents.optionNameValue(message, Component.literal(Math.round(getVolume() * 100) + "%")));
        }

        private float getVolume() {
            return (float) Math.round(Mth.lerp(this.value, 0.0f, 2.0f) * 10) / 10;
        }

        @Override
        protected void applyValue() {
            responder.accept(getVolume());
        }
    }
}
