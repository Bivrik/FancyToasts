package net.bivrik.fancytoasts.client.gui.screen;

import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.gui.Slider;
import net.bivrik.fancytoasts.client.toast.Appearance;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.event.GeneralConfigDataEvent;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;
import static net.bivrik.fancytoasts.client.gui.LayoutValues.PADDING;

public class GeneralConfigScreen extends UniversalScreen {
    private static final Component TITLE = Components.of("gui.config.general_title");
    private static final Component JADE_HIDING_LABEL = Components.of("gui.label.jade_compatibility");
    private static final Component SOUNDS_LABEL = Components.of("gui.label.sounds_enabled");
    private static final Component SCREEN_BEHAVIOR_LABEL = Components.of("gui.label.screen_behavior");
    private static final Component TASK_VOLUME_LABEL = Components.of("gui.label.task_volume");
    private static final Component GOAL_VOLUME_LABEL = Components.of("gui.label.goal_volume");
    private static final Component CHALLENGE_VOLUME_LABEL = Components.of("gui.label.challenge_volume");
    private static final Component POSITION_X_LABEL = Component.literal("Position X");
    private static final Component POSITION_Y_LABEL = Component.literal("Position Y");
    private static final Component LOOPS_STRENGTH_LABEL = Component.literal("Loops Strength");
    private static final Component LOOPS_SPEED_LABEL = Component.literal("Loops Speed");
    private static final Component RESET_LABEL = Components.of("gui.label.reset");
    private static final Component JADE_HIDING_TOOLTIP = Components.of("gui.tooltip.jade_compatibility");
    private static final Component SOUNDS_TOOLTIP = Components.of("gui.tooltip.sounds_enabled");
    private static final Component SCREEN_BEHAVIOR_TOOLTIP = Components.of("gui.tooltip.screen_behavior");
    private static final ResourceLocation LIST_BACKGROUND = ResourceLocations.fromMinecraft("textures/gui/menu_list_background.png");

    private GeneralConfigData generalConfigData;

    private boolean isSaved;
    private long savedFeedbackStartTime;

    private Button doneButton;
    private Button backButton;
    private Button resetButton;
    private CycleButton<Boolean> jadeHidingButton;
    private CycleButton<Boolean> soundsEnabledButton;
    private CycleButton<ToastScreenBehavior> toastScreenBehaviorButton;
    private Slider positionXPercentageSlider;
    private Slider positionYPercentageSlider;
    private Slider loopsStrengthSlider;
    private Slider loopsSpeedSlider;
    private Slider taskVolumeSlider;
    private Slider goalVolumeSlider;
    private Slider challengeVolumeSlider;

    public GeneralConfigScreen(Screen parent) {
        super(TITLE, parent);
        this.generalConfigData = Managers.getConfigManager().getGeneralConfigData();
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2;

        doneButton = this.addFWidget(createButton(CommonComponents.GUI_DONE, button -> done(),
                xCenter + HALF_PADDING, this.height - BUTTON_HEIGHT - 6, 125, BUTTON_HEIGHT));

        backButton = this.addFWidget(createButton(CommonComponents.GUI_BACK, button -> this.toParentScreen(),
                xCenter - 125 - HALF_PADDING, this.height - BUTTON_HEIGHT - 6, 75, BUTTON_HEIGHT));

        resetButton = this.addFWidget(createButton(RESET_LABEL, button -> confirmResetting(),
                xCenter - 50, this.height - BUTTON_HEIGHT - 6, 50, BUTTON_HEIGHT));

        ListHelper listHelper = new ListHelper(this);

        if (Services.PLATFORM.isModLoaded(Constants.Compatibilities.JADE_ID)) {
            jadeHidingButton = listHelper.addWidget(createBooleanButton(JADE_HIDING_LABEL, generalConfigData.isJadeHiding(),
                    (button, value) -> generalConfigData.setJadeHiding(value), 0, 0, Tooltip.create(JADE_HIDING_TOOLTIP)));
        }

        soundsEnabledButton = listHelper.addWidget(createBooleanButton(SOUNDS_LABEL, generalConfigData.areSoundsEnabled(),
                (button, value) -> generalConfigData.setSoundsEnabled(value), 0, 0, Tooltip.create(SOUNDS_TOOLTIP)));

        toastScreenBehaviorButton = listHelper.addWidget(CycleButton.builder(ToastScreenBehavior::getDisplayName)
                .withValues(ToastScreenBehavior.values()).withInitialValue(generalConfigData.getToastScreenBehavior())
                .withTooltip(toastScreenBehavior -> Tooltip.create(SCREEN_BEHAVIOR_TOOLTIP))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, SCREEN_BEHAVIOR_LABEL, (button, value) -> generalConfigData.setToastScreenBehavior(value))
        );

        positionXPercentageSlider = listHelper.addWidget(createSlider(POSITION_X_LABEL, generalConfigData.getPositionXPercentage(),
                this::percentDisplayer, generalConfigData::setPositionXPercentage, 0, 0));

        positionYPercentageSlider = listHelper.addWidget(createSlider(POSITION_Y_LABEL, generalConfigData.getPositionYPercentage(),
                this::percentDisplayer, generalConfigData::setPositionYPercentage, 0, 0));

        loopsStrengthSlider = listHelper.addWidget(createSlider(LOOPS_STRENGTH_LABEL, generalConfigData.getLoopsStrength(), 10.0f, 0.02f,
                this::multiplierDisplayer, generalConfigData::setLoopsStrength, 0, 0));

        loopsSpeedSlider = listHelper.addWidget(createSlider(LOOPS_SPEED_LABEL, generalConfigData.getLoopsSpeed(), 10.0f, 0.02f,
                this::multiplierDisplayer, generalConfigData::setLoopsSpeed, 0, 0));

        taskVolumeSlider = listHelper.addWidget(createSlider(TASK_VOLUME_LABEL, generalConfigData.getTaskVolume(), 2.0f,
                this::percentDisplayer, generalConfigData::setTaskVolume, 0, 0));

        goalVolumeSlider = listHelper.addWidget(createSlider(GOAL_VOLUME_LABEL, generalConfigData.getGoalVolume(), 2.0f,
                this::percentDisplayer, generalConfigData::setGoalVolume, 0, 0));

        challengeVolumeSlider = listHelper.addWidget(createSlider(CHALLENGE_VOLUME_LABEL, generalConfigData.getChallengeVolume(), 2.0f,
                this::percentDisplayer, generalConfigData::setChallengeVolume, 0, 0));

        listHelper.arrangeWidgets();
        listHelper.visitWidgets(this::addFWidget);
    }

    private void confirmResetting() {
        this.openScreen(new ConfirmScreen(this::reset, Components.of("gui.title.reset_confirmation"), Components.of("gui.title.reset_description")));
    }

    private void reset(boolean isConfirmed) {
        this.openScreen(this);

        if (!isConfirmed) {
            return;
        }

        generalConfigData = new GeneralConfigData();
        done();
        this.rebuildWidgets();
    }

    private void done() {
        GeneralConfigData data = generalConfigData.copy();
        if (!data.equals(Managers.getConfigManager().getGeneralConfigData())) {
            ConfigHandler.save(data);
            Managers.getEventManager().changed(new GeneralConfigDataEvent(data));
            isSaved = true;
            savedFeedbackStartTime = Util.getMillis();
        } else {
            this.toParentScreen();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        drawListBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        drawSavedFeedback(guiGraphics, this.width / 2 + PADDING - 25 + BUTTON_WIDTH, this.height - BUTTON_HEIGHT);
    }

    private void drawSavedFeedback(GuiGraphics guiGraphics, int x, int y) {
        if (!isSaved) {
            return;
        }
        long time = Util.getMillis() - savedFeedbackStartTime;

        float appearanceLerp = MathEasing.easeOutLerp(0.0f, 1.0f, Appearance.getProgress(time, 500, 0));
        float disappearanceLerp = Appearance.getProgress(time, 500, 400);

        int color = Colors.alpha(appearanceLerp - disappearanceLerp, Colors.YELLOW);

        guiGraphics.drawString(this.font, "Saved!", x, y, color);

        if (time >= 1000) {
            isSaved = false;
        }
    }

    private void drawListBackground(GuiGraphics guiGraphics) {
        GuiContext context = new GuiContext(guiGraphics);
        context.drawGUITexture(LIST_BACKGROUND, 0, MARGIN, this.width, this.height - MARGIN * 2 - 2, TextureUV.ZERO, 32, 32);
        context.drawGUITexture(Screen.HEADER_SEPARATOR, 0, MARGIN, this.width, 2, TextureUV.ZERO, 32, 2);
        context.drawGUITexture(Screen.FOOTER_SEPARATOR, 0, this.height - MARGIN - 2, this.width, 2, TextureUV.ZERO, 32, 2);
    }

    private Component multiplierDisplayer(float value) {
        return Component.literal("x" + value);
    }

    private Component percentDisplayer(float value) {
        return Component.literal(Math.round(value * 100) + "%");
    }

    private static class ListHelper {
        private final List<AbstractWidget> widgets = new ArrayList<>();
        private final Screen parentScreen;

        private ListHelper(Screen parentScreen) {
            this.parentScreen = parentScreen;
        }

        public <T extends AbstractWidget> T addWidget(T widget) {
            widgets.add(widget);
            return widget;
        }

        public void arrangeWidgets() {
            int y = MARGIN + PADDING;
            int xCenter = parentScreen.width / 2;
            for (int i = 0; i < widgets.size(); i++) {
                int x = xCenter;

                if ((i & 1) == 0) { // Even number - first column + higher than previous row
                    if (i != 0) {
                        y += 20 + PADDING;
                    }

                    x -= BUTTON_WIDTH + HALF_PADDING;
                } else { // Odd number - second column
                    x += HALF_PADDING;
                }

                widgets.get(i).setPosition(x, y);
            }
        }

        public void visitWidgets(Consumer<AbstractWidget> widgetConsumer) {
            widgets.forEach(widgetConsumer);
        }
    }
}
