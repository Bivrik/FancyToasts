package net.bivrik.fancytoasts.client.gui.screen;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.data.ToastsFilteringData;
import net.bivrik.fancytoasts.client.gui.OptionsList;
import net.bivrik.fancytoasts.client.gui.WidgetWidthType;
import net.bivrik.fancytoasts.client.toast.Phase;
import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.core.event.ToastsFilteringDataEvent;
import net.bivrik.fancytoasts.core.manager.ConfigManager;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import static net.bivrik.fancytoasts.client.gui.LayoutValues.*;

public class ToastsFilteringScreen extends UniversalScreen {
    private static final Component TITLE = Components.of("title.toasts_filtering");
    private static final Component RESET_TOASTS_FILTERING_TITLE = Components.of("title.reset_toasts_filtering");
    private static final Component RESET_TOASTS_FILTERING_LABEL = Components.of("label.reset_toasts_filtering");
    private static final Component SAVED_LABEL = Components.of("label.saved");
    private static final Component RESET = Components.of("gui.reset");
    private static final Component FANCY_ADVANCEMENT_TOASTS = Components.of("gui.fancy_advancement_toasts");
    private static final Component ADVANCEMENT_TOASTS = Components.of("gui.advancement_toasts");
    private static final Component FANCY_QUEST_TOASTS = Components.of("gui.fancy_quest_toasts");
    private static final Component RECIPE_TOASTS = Components.of("gui.recipe_toasts");
    private static final Component SYSTEM_TOASTS = Components.of("gui.system_toasts");
    private static final Component TUTORIAL_TOASTS = Components.of("gui.tutorial_toasts");
    private static final Component IGNORED_TOASTS = Components.of("gui.ignored_toasts");
    private static final Component TOASTS_FILTERING_TOOLTIP = Components.of("tooltip.toasts_filtering");

    private final ConfigManager configManager;
    private ToastsFilteringData toastsFilteringData;

    private boolean isSaved;
    private long savedFeedbackStartTime;

    private Button doneButton;
    private Button backButton;
    private Button resetButton;
    private Button toastsFilteringFileButton;
    private CycleButton<Boolean> fancyAdvancementToastsButton;
    private CycleButton<Boolean> fancyQuestToastsButton;
    private CycleButton<Boolean> advancementToastsButton;
    private CycleButton<Boolean> recipeToastsButton;
    private CycleButton<Boolean> systemToastsButton;
    private CycleButton<Boolean> tutorialToastsButton;

    public ToastsFilteringScreen(Screen parent) {
        super(TITLE, parent);

        this.configManager = FancyToasts.getInstance().getConfigManager();
        this.toastsFilteringData = configManager.getToastsFilteringData();
    }

    @Override
    protected void init() {
        addFooter();
        addOptionsList();
    }

    private void addOptionsList() {
        var list = this.addFWidget(new OptionsList(this.minecraft, this.width, this.height - MARGIN * 2 - 2, MARGIN, 25, this));

        fancyAdvancementToastsButton = list.addElement(createBooleanButton(FANCY_ADVANCEMENT_TOASTS, toastsFilteringData.isFancyAdvancementToastsEnabled(),
                (button, value) -> toastsFilteringData.setFancyAdvancementToastsEnabled(value), 0, 0), WidgetWidthType.BIG);

        if (Services.PLATFORM.isModLoaded(Constants.Compatibilities.FTB_QUESTS_ID)) {
            fancyQuestToastsButton = list.addElement(createBooleanButton(FANCY_QUEST_TOASTS, toastsFilteringData.isFancyQuestToastsEnabled(),
                    (button, value) -> toastsFilteringData.setFancyQuestToastsEnabled(value), 0, 0), WidgetWidthType.BIG);
        }

        advancementToastsButton = list.addElement(createBooleanButton(ADVANCEMENT_TOASTS, toastsFilteringData.isAdvancementToastsEnabled(),
                (button, value) -> toastsFilteringData.setAdvancementToastsEnabled(value), 0, 0));

        recipeToastsButton = list.addElement(createBooleanButton(RECIPE_TOASTS, toastsFilteringData.isRecipeToastsEnabled(),
                (button, value) -> toastsFilteringData.setRecipeToastsEnabled(value), 0, 0));

        systemToastsButton = list.addElement(createBooleanButton(SYSTEM_TOASTS, toastsFilteringData.isSystemToastsEnabled(),
                (button, value) -> toastsFilteringData.setSystemToastsEnabled(value), 0, 0));

        tutorialToastsButton = list.addElement(createBooleanButton(TUTORIAL_TOASTS, toastsFilteringData.isTutorialToastsEnabled(),
                (button, value) -> toastsFilteringData.setTutorialToastsEnabled(value), 0, 0));

        toastsFilteringFileButton = list.addElement(createButton(IGNORED_TOASTS, button -> openToastsFilteringFile(),
                0, 0, Tooltip.create(TOASTS_FILTERING_TOOLTIP)), WidgetWidthType.BIG);

        list.alignElements();
    }

    private void addFooter() {
        LinearLayout layout = new LinearLayout(this.width / 2 - 250 / 2, this.height - BUTTON_HEIGHT - 6, 250 + PADDING * 2, BUTTON_HEIGHT, LinearLayout.Orientation.HORIZONTAL);

        backButton = layout.addChild(createButton(CommonComponents.GUI_BACK, button -> this.toParentScreen(), 0, 0, 75, BUTTON_HEIGHT));
        resetButton = layout.addChild(createButton(RESET, button -> confirmResetting(), 0, 0, 50, BUTTON_HEIGHT));
        doneButton = layout.addChild(createButton(CommonComponents.GUI_DONE, button -> done(), 0, 0, 125, BUTTON_HEIGHT));

        layout.arrangeElements();
        layout.visitWidgets(this::addFWidget);
    }

    private void openToastsFilteringFile() {
        Util.getPlatform().openUri(Paths.actualPath(Paths.TOASTS_FILTERING_FILE).toUri());
    }

    private void confirmResetting() {
        this.openScreen(new ConfirmScreen(this::reset, RESET_TOASTS_FILTERING_TITLE, RESET_TOASTS_FILTERING_LABEL));
    }

    private void reset(boolean isConfirmed) {
        this.openScreen(this);

        if (!isConfirmed) {
            return;
        }

        toastsFilteringData = new ToastsFilteringData();
        save(toastsFilteringData.copy());
        this.rebuildWidgets();
    }

    private void done() {
        ToastsFilteringData data = toastsFilteringData.copy();
        if (!data.equals(configManager.getToastsFilteringData())) {
            save(data);
        } else {
            this.toParentScreen();
        }
    }

    private void save(ToastsFilteringData data) {
        ConfigHandler.save(data);
        FancyToasts.EVENTS.sendEvent(new ToastsFilteringDataEvent(data));
        isSaved = true;
        savedFeedbackStartTime = Util.getMillis();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        drawSavedFeedback(guiGraphics, doneButton.getX() + doneButton.getWidth() + PADDING, this.height - BUTTON_HEIGHT);
    }

    private void drawSavedFeedback(GuiGraphics guiGraphics, int x, int y) {
        if (!isSaved) {
            return;
        }
        long time = Util.getMillis() - savedFeedbackStartTime;

        float appearanceLerp = Easing.OCT_EASE_OUT.lerp(0, 1.0f, Phase.getProgress(time, 500, 0));
        float disappearanceLerp = Phase.getProgress(time, 500, 400);

        Color color = Color.YELLOW.withAlpha(appearanceLerp - disappearanceLerp);

        guiGraphics.drawString(this.font, SAVED_LABEL, x, y, color.getARGB());

        if (time >= 850) {
            isSaved = false;
        }
    }
}
