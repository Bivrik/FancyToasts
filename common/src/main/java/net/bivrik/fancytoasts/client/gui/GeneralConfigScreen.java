package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.AdvancementToastPosition;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.GeneralConfigData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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
    private CycleButton<AdvancementToastPosition> advancementToastPositionCycleButton; // what are those names bro
    private CycleButton<AdvancementToastScreenBehavior> advancementToastScreenBehaviorCycleButton; // I swear next update is just refactor what is THIS BRO

    private final List<AbstractWidget> widgets = new ArrayList<>();

    public GeneralConfigScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
        this.generalConfigData = Common.getConfigManager().getGeneralConfig();
    }

    private void addWidget(AbstractWidget aw) {
        widgets.add(aw);
    }

    @Override
    protected void init() {
        doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> done())
                .bounds(this.width / 2 + 100 - 125 + PADDING / 2, this.height - 26, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> toParentScreen())
                .bounds(this.width / 2 - 125 - PADDING / 2, this.height - 20 - 6, 100, BUTTON_HEIGHT).build());

        jadeCompatCycleButton = CycleButton.onOffBuilder()
                .withInitialValue(generalConfigData.isJadeCompatEnabled())
                .withTooltip((value) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.jade_compatibility")))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.jade_compatibility"), (button, value) -> generalConfigData.setJadeCompatEnabled(value));

        soundsEnabledCycleButton = CycleButton.onOffBuilder()
                .withInitialValue(generalConfigData.areSoundsEnabled())
                .withTooltip((value) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.sounds_enabled")))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.sounds_enabled"), (button, value) -> generalConfigData.setSoundsEnabled(value));

        advancementToastPositionCycleButton = CycleButton.builder(AdvancementToastPosition::getDisplayName)
                .withValues(AdvancementToastPosition.values()).withInitialValue(generalConfigData.getPosition())
                .withTooltip((position) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.position")))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.position"), (button, value) -> generalConfigData.setPosition(value));

        advancementToastScreenBehaviorCycleButton = CycleButton.builder(AdvancementToastScreenBehavior::getDisplayName)
                .withValues(AdvancementToastScreenBehavior.values()).withInitialValue(generalConfigData.getScreenBehavior())
                .withTooltip((position) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.screen_behavior")))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.screen_behavior"), (button, value) -> generalConfigData.setScreenBehavior(value));

        taskVolumeSlider = new VolumeSlider(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.task_volume"), generalConfigData.getTaskVolume());
        taskVolumeSlider.setResponder(generalConfigData::setTaskVolume);

        goalVolumeSlider = new VolumeSlider(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.goal_volume"), generalConfigData.getGoalVolume());
        goalVolumeSlider.setResponder(generalConfigData::setGoalVolume);

        challengeVolumeSlider = new VolumeSlider(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.challenge_volume"), generalConfigData.getChallengeVolume());
        challengeVolumeSlider.setResponder(generalConfigData::setChallengeVolume);

        addWidget(jadeCompatCycleButton);
        addWidget(soundsEnabledCycleButton);
        addWidget(advancementToastPositionCycleButton);
        addWidget(advancementToastScreenBehaviorCycleButton);
        addWidget(taskVolumeSlider);
        addWidget(goalVolumeSlider);
        addWidget(challengeVolumeSlider);

        int y = MARGIN + PADDING;
        for (int i = 0; i < widgets.size(); i++) {
            int x = this.width / 2 - BUTTON_WIDTH - PADDING / 2;

            if ((i & 1) == 0) {
                if (i != 0) {
                    y += 20 + PADDING;
                }
            }
            else {
                x += BUTTON_WIDTH + PADDING;
            }

            AbstractWidget widget = widgets.get(i);

            widget.setX(x);
            widget.setY(y);

            this.addRenderableWidget(widget);
        }
    }

    @Override
    protected void rebuildWidgets() {
        widgets.clear();
        super.rebuildWidgets();
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
        var resourcelocation = ResourceLocation.withDefaultNamespace("textures/gui/menu_list_background.png");
        graphics.blit(RenderPipelines.GUI_TEXTURED, resourcelocation, 0, MARGIN, 0, 0, this.width, this.height - MARGIN * 2 - 2, 32, 32);
        graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, 0, MARGIN, 0, 0, this.width, 2, 32, 2);
        graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - MARGIN - 2, 0, 0, width, 2, 32, 2);

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
            return (float) Math.round(Mth.lerp(this.value, 0.0f, 2.0f) * 100) / 100;
        }

        @Override
        protected void applyValue() {
            responder.accept(getVolume());
        }
    }
}
