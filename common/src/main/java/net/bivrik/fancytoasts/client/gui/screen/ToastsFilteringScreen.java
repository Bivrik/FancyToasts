package net.bivrik.fancytoasts.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.data.ToastsFilteringData;
import net.bivrik.fancytoasts.client.toast.Appearance;
import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.event.ToastsFilteringDataEvent;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
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

    private static final ResourceLocation LIST_BACKGROUND = ResourceLocations.fromMinecraft("textures/gui/menu_list_background.png");

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
        this.toastsFilteringData = Managers.getConfigManager().getToastsFilteringData();
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

        fancyAdvancementToastsButton = listHelper.addWidget(createBooleanButton(FANCY_ADVANCEMENT_TOASTS, toastsFilteringData.isFancyAdvancementToastsEnabled(),
                (button, value) -> toastsFilteringData.setFancyAdvancementToastsEnabled(value), 0, 0));

        if (Services.PLATFORM.isModLoaded(Constants.Compatibilities.FTB_QUESTS_ID)) {
            fancyQuestToastsButton = listHelper.addWidget(createBooleanButton(FANCY_QUEST_TOASTS, toastsFilteringData.isFancyQuestToastsEnabled(),
                    (button, value) -> toastsFilteringData.setFancyQuestToastsEnabled(value), 0, 0));
        }

        advancementToastsButton = listHelper.addWidget(createBooleanButton(ADVANCEMENT_TOASTS, toastsFilteringData.isAdvancementToastsEnabled(),
                (button, value) -> toastsFilteringData.setAdvancementToastsEnabled(value), 0, 0));

        recipeToastsButton = listHelper.addWidget(createBooleanButton(RECIPE_TOASTS, toastsFilteringData.isRecipeToastsEnabled(),
                (button, value) -> toastsFilteringData.setRecipeToastsEnabled(value), 0, 0));

        systemToastsButton = listHelper.addWidget(createBooleanButton(SYSTEM_TOASTS, toastsFilteringData.isSystemToastsEnabled(),
                (button, value) -> toastsFilteringData.setSystemToastsEnabled(value), 0, 0));

        tutorialToastsButton = listHelper.addWidget(createBooleanButton(TUTORIAL_TOASTS, toastsFilteringData.isTutorialToastsEnabled(),
                (button, value) -> toastsFilteringData.setTutorialToastsEnabled(value), 0, 0));

        toastsFilteringFileButton = listHelper.addWidget(createButton(IGNORED_TOASTS, button -> openToastsFilteringFile(),
                0, 0, Tooltip.create(TOASTS_FILTERING_TOOLTIP)));

        listHelper.arrangeWidgets();
        listHelper.visitWidgets(this::addFWidget);
    }

    private void openToastsFilteringFile() {
        Util.getPlatform().openPath(Paths.actualPath(Paths.TOASTS_FILTERING_FILE));
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
        if (!data.equals(Managers.getConfigManager().getToastsFilteringData())) {
            save(data);
        } else {
            this.toParentScreen();
        }
    }

    private void save(ToastsFilteringData data) {
        ConfigHandler.save(data);
        Managers.getEventManager().sendEvent(new ToastsFilteringDataEvent(data));
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
    }

    private void drawSavedFeedback(GuiGraphics guiGraphics, int x, int y) {
        if (!isSaved) {
            return;
        }
        long time = Util.getMillis() - savedFeedbackStartTime;

        float appearanceLerp = Easing.OCT_EASE_OUT.lerp(0, 1.0f, Appearance.getProgress(time, 500, 0));
        float disappearanceLerp = Appearance.getProgress(time, 500, 400);

        Color color = Color.YELLOW.withAlpha(appearanceLerp - disappearanceLerp);

        guiGraphics.drawString(this.font, SAVED_LABEL, x, y, color.getARGB());

        if (time >= 1000) {
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
