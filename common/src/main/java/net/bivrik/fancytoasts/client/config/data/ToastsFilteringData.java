package net.bivrik.fancytoasts.client.config.data;

import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ToastsFilteringData extends ConfigData {
    private boolean fancyAdvancementToastsEnabled;
    private boolean fancyQuestToastsEnabled;
    private boolean advancementToastsEnabled;
    private boolean recipeToastsEnabled;
    private boolean systemToastsEnabled;
    private boolean tutorialToastsEnabled;

    // Only set on load, changing through config file
    private final Map<FancyToastType, Boolean> typesToIgnore = new EnumMap<>(FancyToastType.class);
    private final List<String> toastsToIgnore = new ArrayList<>();

    private final transient Set<String> exactMatches = new HashSet<>();
    private final transient Set<String> prefixMatches = new HashSet<>();

    public ToastsFilteringData(boolean fancyAdvancementToastsEnabled, boolean fancyQuestToastsEnabled, boolean advancementToastsEnabled, boolean recipeToastsEnabled, boolean systemToastsEnabled, boolean tutorialToastsEnabled, Map<FancyToastType, Boolean> typesToIgnore, List<String> toastsToIgnore) {
        super(Paths.TOASTS_FILTERING_FILE);

        this.fancyAdvancementToastsEnabled = fancyAdvancementToastsEnabled;
        this.fancyQuestToastsEnabled = fancyQuestToastsEnabled;
        this.advancementToastsEnabled = advancementToastsEnabled;
        this.recipeToastsEnabled = recipeToastsEnabled;
        this.systemToastsEnabled = systemToastsEnabled;
        this.tutorialToastsEnabled = tutorialToastsEnabled;

        this.typesToIgnore.putAll(typesToIgnore);
        this.toastsToIgnore.addAll(toastsToIgnore);
        for (String toastToIgnore : this.toastsToIgnore) {
            if (toastToIgnore.endsWith("/...")) {
                prefixMatches.add(toastToIgnore.replace("/...", ""));
            } else {
                exactMatches.add(toastToIgnore);
            }
        }
    }

    public ToastsFilteringData() {
        this(true, true, true, true, true, true, Map.of(
                FancyToastType.TASK, false,
                FancyToastType.GOAL, false,
                FancyToastType.CHALLENGE, false
        ), new ArrayList<>());
    }

    public boolean isTypeIgnored(FancyToastType type) {
        return typesToIgnore.get(type);
    }

    public boolean isToastIgnored(ResourceLocation toastLocation) {
        String toast = toastLocation.toString();

        if (exactMatches.contains(toast)) {
            return true;
        }

        for (String prefix : prefixMatches) {
            if (toast.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    public boolean isFancyAdvancementToastsEnabled() {
        return fancyAdvancementToastsEnabled;
    }
    public void setFancyAdvancementToastsEnabled(boolean fancyAdvancementToastsEnabled) {
        this.fancyAdvancementToastsEnabled = fancyAdvancementToastsEnabled;
    }

    public boolean isFancyQuestToastsEnabled() {
        return fancyQuestToastsEnabled;
    }
    public void setFancyQuestToastsEnabled(boolean fancyQuestToastsEnabled) {
        this.fancyQuestToastsEnabled = fancyQuestToastsEnabled;
    }

    public boolean isAdvancementToastsEnabled() {
        return advancementToastsEnabled;
    }
    public void setAdvancementToastsEnabled(boolean advancementToastsEnabled) {
        this.advancementToastsEnabled = advancementToastsEnabled;
    }

    public boolean isRecipeToastsEnabled() {
        return recipeToastsEnabled;
    }
    public void setRecipeToastsEnabled(boolean recipeToastsEnabled) {
        this.recipeToastsEnabled = recipeToastsEnabled;
    }

    public boolean isSystemToastsEnabled() {
        return systemToastsEnabled;
    }
    public void setSystemToastsEnabled(boolean systemToastsEnabled) {
        this.systemToastsEnabled = systemToastsEnabled;
    }

    public boolean isTutorialToastsEnabled() {
        return tutorialToastsEnabled;
    }
    public void setTutorialToastsEnabled(boolean tutorialToastsEnabled) {
        this.tutorialToastsEnabled = tutorialToastsEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToastsFilteringData that)) return false;
        return fancyAdvancementToastsEnabled == that.fancyAdvancementToastsEnabled && fancyQuestToastsEnabled == that.fancyQuestToastsEnabled && advancementToastsEnabled == that.advancementToastsEnabled && recipeToastsEnabled == that.recipeToastsEnabled && systemToastsEnabled == that.systemToastsEnabled && tutorialToastsEnabled == that.tutorialToastsEnabled && Objects.equals(typesToIgnore, that.typesToIgnore) && Objects.equals(toastsToIgnore, that.toastsToIgnore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, advancementToastsEnabled, recipeToastsEnabled, systemToastsEnabled, tutorialToastsEnabled, typesToIgnore, toastsToIgnore);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public ToastsFilteringData copy() {
        return new ToastsFilteringData(fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, advancementToastsEnabled, recipeToastsEnabled, systemToastsEnabled, tutorialToastsEnabled, typesToIgnore, toastsToIgnore);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "fancyAdvancementToastsEnabled='%s', fancyQuestsToastsEnabled='%s', advancementToastsEnabled='%s', recipeToastsEnabled='%s', systemToastsEnabled='%s', tutorialToastsEnabled='%s', typesToIgnore='%s', toastsToIgnore='%s'}",
                fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, advancementToastsEnabled, recipeToastsEnabled, systemToastsEnabled, tutorialToastsEnabled, typesToIgnore, toastsToIgnore
        );
    }
}
