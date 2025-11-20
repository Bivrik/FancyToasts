package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.config.AdvancementToastPosition;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.TextureUV;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.Managers;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.bivrik.fancytoasts.client.ui.LayoutValues.*;
import static net.bivrik.fancytoasts.client.ui.LayoutValues.PADDING;

public class GeneralConfigScreen extends UniversalScreen {
    private static final Component TITLE = Component.translatable(Constants.MOD_ID + ".gui.config.general_title");
    private final GeneralConfigData generalConfigData;

    private Button doneButton;
    private Button backButton;
    private CycleButton<Boolean> jadeCompatCycleButton;
    private CycleButton<Boolean> soundsEnabledCycleButton;
    private VolumeSlider taskVolumeSlider;
    private VolumeSlider goalVolumeSlider;
    private VolumeSlider challengeVolumeSlider;
    private CycleButton<AdvancementToastPosition> positionCycButton;
    private CycleButton<AdvancementToastScreenBehavior> screenBehaviorCycButton;

    private final List<AbstractWidget> widgets = new ArrayList<>();

    public GeneralConfigScreen(Screen parent) {
        super(TITLE, parent);
        this.generalConfigData = Managers.configManager().generalConfig();
    }

    private void addWidget(AbstractWidget aw) {
        widgets.add(aw);
    }

    @Override
    protected void init() {
        doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> done())
                .bounds(this.width / 2 + 100 - 125 + PADDING / 2, this.height - 26, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        backButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> this.toParentScreen())
                .bounds(this.width / 2 - 125 - PADDING / 2, this.height - 20 - 6, 100, BUTTON_HEIGHT).build());

        if (Services.PLATFORM.isModLoaded(Constants.Compatibilities.JADE_ID)) {
            jadeCompatCycleButton = CycleButton.onOffBuilder()
                    .withInitialValue(generalConfigData.isJadeCompatEnabled())
                    .withTooltip((value) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.jade_compatibility")))
                    .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.jade_compatibility"), (button, value) -> generalConfigData.setJadeCompatEnabled(value));
        }

        soundsEnabledCycleButton = CycleButton.onOffBuilder()
                .withInitialValue(generalConfigData.areSoundsEnabled())
                .withTooltip((value) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.sounds_enabled")))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.sounds_enabled"), (button, value) -> generalConfigData.setSoundsEnabled(value));

        positionCycButton = CycleButton.builder(AdvancementToastPosition::getDisplayName)
                .withValues(AdvancementToastPosition.values()).withInitialValue(generalConfigData.getPosition())
                .withTooltip((position) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.position")))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.position"), (button, value) -> generalConfigData.setPosition(value));

        screenBehaviorCycButton = CycleButton.builder(AdvancementToastScreenBehavior::getDisplayName)
                .withValues(AdvancementToastScreenBehavior.values()).withInitialValue(generalConfigData.getScreenBehavior())
                .withTooltip((position) -> Tooltip.create(Component.translatable("fancytoasts.gui.tooltip.screen_behavior")))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.screen_behavior"), (button, value) -> generalConfigData.setScreenBehavior(value));

        taskVolumeSlider = new VolumeSlider(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.task_volume"), generalConfigData.getTaskVolume());
        taskVolumeSlider.setResponder(generalConfigData::setTaskVolume);

        goalVolumeSlider = new VolumeSlider(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.goal_volume"), generalConfigData.getGoalVolume());
        goalVolumeSlider.setResponder(generalConfigData::setGoalVolume);

        challengeVolumeSlider = new VolumeSlider(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("fancytoasts.gui.label.challenge_volume"), generalConfigData.getChallengeVolume());
        challengeVolumeSlider.setResponder(generalConfigData::setChallengeVolume);

        if (Services.PLATFORM.isModLoaded(Constants.Compatibilities.JADE_ID)) {
            addWidget(jadeCompatCycleButton);
        }
        addWidget(soundsEnabledCycleButton);
        addWidget(positionCycButton);
        addWidget(screenBehaviorCycButton);
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

            if (widget != null) {
                widget.setX(x);
                widget.setY(y);

                this.addRenderableWidget(widget);
            }
        }
    }

    @Override
    protected void rebuildWidgets() {
        widgets.clear();
        super.rebuildWidgets();
    }

    private void done() {
        ConfigHandler.save(generalConfigData);
        this.toParentScreen();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var textureLocation = ResourceLocations.fromMinecraft("textures/gui/menu_list_background.png");

        GuiContext context = new GuiContext(guiGraphics);
        context.drawGUITexture(textureLocation, 0, MARGIN, this.width, this.height - MARGIN * 2 - 2, TextureUV.ZERO, 32, 32);
        context.drawGUITexture(Screen.HEADER_SEPARATOR, 0, MARGIN, this.width, 2, TextureUV.ZERO, 32, 2);
        context.drawGUITexture(Screen.FOOTER_SEPARATOR, 0, this.height - MARGIN - 2, this.width, 2, TextureUV.ZERO, 32, 2);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
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
