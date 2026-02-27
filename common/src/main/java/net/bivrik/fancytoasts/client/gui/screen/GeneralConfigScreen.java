package net.bivrik.fancytoasts.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancytoasts.client.config.ToastAnchor;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.gui.IntegerEditBox;
import net.bivrik.fancytoasts.client.gui.Slider;
import net.bivrik.fancytoasts.client.toast.Appearance;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.event.GeneralConfigDataEvent;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.bivrik.fancytoasts.utility.Easing;
import net.bivrik.fancytoasts.utility.FastMath;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.utility.TextureUV;
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
    private static final Component TITLE = Components.of("title.general_settings");
    private static final Component RESET_GENERAL_SETTINGS_TITLE = Components.of("title.reset_general_settings");
    private static final Component RESET_GENERAL_SETTINGS_LABEL = Components.of("label.reset_general_settings");
    private static final Component SAVED_LABEL = Components.of("label.saved");
    private static final Component JADE_HIDING = Components.of("gui.jade_hiding");
    private static final Component BOSS_BAR_HIDING = Components.of("gui.boss_bar_hiding");
    private static final Component SOUNDS = Components.of("gui.sounds_enabled");
    private static final Component SCREEN_BEHAVIOR = Components.of("gui.screen_behavior");
    private static final Component TASK_VOLUME = Components.of("gui.task_volume");
    private static final Component GOAL_VOLUME = Components.of("gui.goal_volume");
    private static final Component CHALLENGE_VOLUME = Components.of("gui.challenge_volume");
    private static final Component LOOPS_STRENGTH = Components.of("gui.loops_strength");
    private static final Component LOOPS_SPEED = Components.of("gui.loops_speed");
    private static final Component PITCH_RANDOMNESS = Components.of("gui.pitch_randomness");
    private static final Component ANIMATION_SPEED = Components.of("gui.animation_speed");
    private static final Component RESET = Components.of("gui.reset");
    private static final Component ANCHOR = Components.of("gui.anchor");
    private static final Component JADE_HIDING_TOOLTIP = Components.of("tooltip.jade_hiding");
    private static final Component BOSS_BAR_HIDING_TOOLTIP = Components.of("tooltip.boss_bar_hiding");
    private static final Component SOUNDS_TOOLTIP = Components.of("tooltip.sounds_enabled");
    private static final Component SCREEN_BEHAVIOR_TOOLTIP = Components.of("tooltip.screen_behavior");
    private static final Component ANCHOR_TOOLTIP = Components.of("tooltip.anchor");
    private static final Component LOOPS_STRENGTH_TOOLTIP = Components.of("tooltip.loops_strength");
    private static final Component LOOPS_SPEED_TOOLTIP = Components.of("tooltip.loops_speed");
    private static final Component PITCH_RANDOMNESS_TOOLTIP = Components.of("tooltip.pitch_randomness");
    private static final Component ANIMATION_SPEED_TOOLTIP = Components.of("tooltip.animation_speed");

    private static final ResourceLocation LIST_BACKGROUND = ResourceLocations.fromMinecraft("textures/gui/menu_list_background.png");

    private GeneralConfigData generalConfigData;
    private boolean isSaved;
    private long savedFeedbackStartTime;

    private Button doneButton;
    private Button backButton;
    private Button resetButton;
    private CycleButton<Boolean> jadeHidingButton;
    private CycleButton<Boolean> bossBarHidingButton;
    private CycleButton<Boolean> soundsEnabledButton;
    private CycleButton<ToastScreenBehavior> toastScreenBehaviorButton;
    private CycleButton<ToastAnchor> toastAnchorButton;
    private Slider loopsStrengthSlider;
    private Slider loopsSpeedSlider;
    private Slider pitchRandomnessSlider;
    private Slider animationSpeedSlider;
    private Slider taskVolumeSlider;
    private Slider goalVolumeSlider;
    private Slider challengeVolumeSlider;
    private IntegerEditBox offsetXEditBox;
    private IntegerEditBox offsetYEditBox;

    public GeneralConfigScreen(Screen parent) {
        super(TITLE, parent);
        this.generalConfigData = Managers.getConfigManager().getGeneralConfigData();
    }

    @Override
    protected void init() {
        int xCenter = this.width / 2;

        backButton = this.addFWidget(createButton(CommonComponents.GUI_BACK, button -> this.toParentScreen(),
                xCenter - 125 - HALF_PADDING, this.height - BUTTON_HEIGHT - 6, 75, BUTTON_HEIGHT));

        resetButton = this.addFWidget(createButton(RESET, button -> confirmResetting(),
                xCenter - 50, this.height - BUTTON_HEIGHT - 6, 50, BUTTON_HEIGHT));

        doneButton = this.addFWidget(createButton(CommonComponents.GUI_DONE, button -> done(),
                xCenter + HALF_PADDING, this.height - BUTTON_HEIGHT - 6, 125, BUTTON_HEIGHT));

        ListHelper listHelper = new ListHelper(this);

        if (Services.PLATFORM.isModLoaded(Constants.Compatibilities.JADE_ID)) {
            jadeHidingButton = listHelper.addWidget(createBooleanButton(JADE_HIDING, generalConfigData.isJadeHiding(),
                    (button, value) -> generalConfigData.setJadeHiding(value), 0, 0, Tooltip.create(JADE_HIDING_TOOLTIP)));
        }

        bossBarHidingButton = listHelper.addWidget(createBooleanButton(BOSS_BAR_HIDING, generalConfigData.isBossBarHiding(),
                (button, value) -> generalConfigData.setBossBarHiding(value), 0, 0, Tooltip.create(BOSS_BAR_HIDING_TOOLTIP)));

        soundsEnabledButton = listHelper.addWidget(createBooleanButton(SOUNDS, generalConfigData.areSoundsEnabled(),
                (button, value) -> generalConfigData.setSoundsEnabled(value), 0, 0, Tooltip.create(SOUNDS_TOOLTIP)));

        toastScreenBehaviorButton = listHelper.addWidget(CycleButton.builder(ToastScreenBehavior::getDisplayName)
                .withValues(ToastScreenBehavior.values()).withInitialValue(generalConfigData.getToastScreenBehavior())
                .withTooltip(toastScreenBehavior -> Tooltip.create(SCREEN_BEHAVIOR_TOOLTIP))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, SCREEN_BEHAVIOR, (button, value) -> generalConfigData.setToastScreenBehavior(value))
        );

        toastAnchorButton = listHelper.addWidget(CycleButton.builder(ToastAnchor::getDisplayName)
                .withValues(ToastAnchor.values()).withInitialValue(generalConfigData.getToastAnchor())
                .withTooltip(toastAnchor -> Tooltip.create(ANCHOR_TOOLTIP))
                .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, ANCHOR, (button, value) -> changeToastAnchor(value)));

        offsetXEditBox = listHelper.addWidget(new IntegerEditBox(this.font, 0, 0, HALF_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT, this.offsetXEditBox, Component.empty(), generalConfigData.getOffsetX()));
        offsetXEditBox.setResponder(value -> offsetXEditBox.setIntegerResponder(generalConfigData::setOffsetX));

        offsetYEditBox = listHelper.addWidget(new IntegerEditBox(this.font, 0, 0, HALF_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT, this.offsetYEditBox, Component.empty(), generalConfigData.getOffsetY()));
        offsetYEditBox.setResponder(value -> offsetYEditBox.setIntegerResponder(generalConfigData::setOffsetY));

        loopsStrengthSlider = listHelper.addWidget(createSlider(LOOPS_STRENGTH, generalConfigData.getLoopsStrength(), 10.0f, 0.02f,
                this::multiplierDisplayer, generalConfigData::setLoopsStrength, 0, 0, HALF_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT, Tooltip.create(LOOPS_STRENGTH_TOOLTIP)));

        loopsSpeedSlider = listHelper.addWidget(createSlider(LOOPS_SPEED, generalConfigData.getLoopsSpeed(), 10.0f, 0.02f,
                this::multiplierDisplayer, generalConfigData::setLoopsSpeed, 0, 0, HALF_BUTTON_WIDTH - HALF_PADDING, BUTTON_HEIGHT, Tooltip.create(LOOPS_SPEED_TOOLTIP)));

        pitchRandomnessSlider = listHelper.addWidget(createSlider(PITCH_RANDOMNESS, generalConfigData.getPitchRandomness(), 0.2f, 0,
                this::percentDisplayer, generalConfigData::setPitchRandomness, 0, 0, Tooltip.create(PITCH_RANDOMNESS_TOOLTIP)));

        animationSpeedSlider = listHelper.addWidget(createSlider(ANIMATION_SPEED, generalConfigData.getAnimationSpeed(), 0.5f, 3.0f, 0.02f,
                this::multiplierDisplayer, generalConfigData::setAnimationSpeed, 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Tooltip.create(ANIMATION_SPEED_TOOLTIP)));

        taskVolumeSlider = listHelper.addWidget(createSlider(TASK_VOLUME, generalConfigData.getTaskVolume(), 2.0f,
                this::percentDisplayer, generalConfigData::setTaskVolume, 0, 0));

        goalVolumeSlider = listHelper.addWidget(createSlider(GOAL_VOLUME, generalConfigData.getGoalVolume(), 2.0f,
                this::percentDisplayer, generalConfigData::setGoalVolume, 0, 0));

        challengeVolumeSlider = listHelper.addWidget(createSlider(CHALLENGE_VOLUME, generalConfigData.getChallengeVolume(), 2.0f,
                this::percentDisplayer, generalConfigData::setChallengeVolume, 0, 0));

        listHelper.arrangeWidgets();
        listHelper.visitWidgets(this::addFWidget);
    }

    private void changeToastAnchor(ToastAnchor anchor) {
        generalConfigData.setToastAnchor(anchor);
        offsetXEditBox.setIntegerValue(anchor.getBaseOffsetX());
        offsetYEditBox.setIntegerValue(anchor.getBaseOffsetY());
    }

    private void confirmResetting() {
        this.openScreen(new ConfirmScreen(this::reset, RESET_GENERAL_SETTINGS_TITLE, RESET_GENERAL_SETTINGS_LABEL));
    }

    private void reset(boolean isConfirmed) {
        this.openScreen(this);

        if (!isConfirmed) {
            return;
        }

        generalConfigData = new GeneralConfigData();
        save(generalConfigData.copy());
        this.rebuildWidgets();
    }

    private void done() {
        GeneralConfigData data = generalConfigData.copy();
        if (!data.equals(Managers.getConfigManager().getGeneralConfigData())) {
            save(data);
        } else {
            this.toParentScreen();
        }
    }

    private void save(GeneralConfigData data) {
        ConfigHandler.save(data);
        Managers.getEventManager().sendEvent(new GeneralConfigDataEvent(data));
        isSaved = true;
        savedFeedbackStartTime = Util.getMillis();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.drawTitle(guiGraphics);
        drawListBackground(guiGraphics);
        this.drawRenderables(guiGraphics, mouseX, mouseY, partialTick);
        drawSavedFeedback(guiGraphics, this.width / 2 + PADDING - 25 + BUTTON_WIDTH, this.height - BUTTON_HEIGHT);
        drawPositionHints(guiGraphics);
    }

    private void drawPositionHints(GuiGraphics guiGraphics) {
        if (offsetXEditBox == null || offsetYEditBox == null) {
            return;
        }

        int offsetX = 7;
        int offsetY = 5;
        guiGraphics.drawString(this.font, "x:", offsetXEditBox.getX() - offsetX, offsetXEditBox.getY() + offsetY, Colors.LIGHT_GRAY);
        guiGraphics.drawString(this.font, "y:", offsetYEditBox.getX() - offsetX, offsetYEditBox.getY() + offsetY, Colors.LIGHT_GRAY);
    }

    private void drawSavedFeedback(GuiGraphics guiGraphics, int x, int y) {
        if (!isSaved) {
            return;
        }
        long time = Util.getMillis() - savedFeedbackStartTime;

        float appearanceLerp = Easing.OCT_EASE_OUT.lerp(0, 1.0f, Appearance.getProgress(time, 500, 0));
        float disappearanceLerp = Appearance.getProgress(time, 500, 400);

        int color = Colors.alpha(appearanceLerp - disappearanceLerp, Colors.YELLOW);
        guiGraphics.drawString(this.font, SAVED_LABEL, x, y, color);

        if (time > 850) {
            isSaved = false;
        }
    }

    private void drawListBackground(GuiGraphics guiGraphics) {
        GuiContext context = new GuiContext(guiGraphics);
        RenderSystem.enableBlend();
        context.drawGUITexture(LIST_BACKGROUND, 0, MARGIN, this.width, this.height - MARGIN * 2 - 2, TextureUV.ZERO, 32, 32);
        context.drawGUITexture(Screen.HEADER_SEPARATOR, 0, MARGIN, this.width, 2, TextureUV.ZERO, 32, 2);
        context.drawGUITexture(Screen.FOOTER_SEPARATOR, 0, this.height - MARGIN - 2, this.width, 2, TextureUV.ZERO, 32, 2);
        RenderSystem.disableBlend();
    }

    private Component multiplierDisplayer(float value) {
        return Component.literal("x" + value);
    }

    private Component percentDisplayer(float value) {
        return Component.literal(FastMath.round(value * 100) + "%");
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
            int neighbours = 0; // Works only if two "neighbours" go together one after another
            int numMinus = 0;
            for (int i = 0; i < widgets.size(); i++) {
                var widget = widgets.get(i);
                int x = xCenter;

                if (widget.getWidth() != BUTTON_WIDTH) {
                    neighbours++;
                }

                if (neighbours == 2) {
                    x += HALF_BUTTON_WIDTH + HALF_PADDING;
                    i--;
                }

                i -= numMinus;
                if ((i & 1) == 0) { // Even number - first column + higher than previous row
                    if (i != 0 && neighbours != 2) {
                        y += 20 + PADDING;
                    }

                    x -= BUTTON_WIDTH + HALF_PADDING;
                } else { // Odd number - second column
                    x += HALF_PADDING;
                }
                i += numMinus;

                if (neighbours == 2) {
                    i++;
                    numMinus++;
                    neighbours = 0;
                }

                widget.setPosition(x, y);
            }
        }

        public void visitWidgets(Consumer<AbstractWidget> widgetConsumer) {
            widgets.forEach(widgetConsumer);
        }
    }
}
