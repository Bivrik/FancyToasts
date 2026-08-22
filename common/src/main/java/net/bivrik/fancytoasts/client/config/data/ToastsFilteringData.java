package net.bivrik.fancytoasts.client.config.data;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.FancyQuestType;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.resources.ResourceLocation;

public class ToastsFilteringData extends ConfigData {
    private static final int VERSION = Constants.ConfigVersions.TOAST_FILTERING;

    private boolean fancyAdvancementToastsEnabled;
    private boolean fancyQuestToastsEnabled;

    // Only set on load, changing through config file
    private final Map<FancyToastType, Boolean> typesToIgnore = new EnumMap<>(FancyToastType.class);
    private final Map<FancyQuestType, Boolean> questTypesToIgnore = new EnumMap<>(FancyQuestType.class);
    private final List<String> toastsToIgnore = new ArrayList<>();

    private final transient Set<String> exactMatches = new HashSet<>();
    private final transient Set<String> prefixMatches = new HashSet<>();

    private ToastsFilteringData(boolean fancyAdvancementToastsEnabled, boolean fancyQuestToastsEnabled, Map<FancyToastType, Boolean> typesToIgnore, List<String> toastsToIgnore, Map<FancyQuestType, Boolean> questTypesToIgnore) {
        super(Paths.TOASTS_FILTERING_FILE);

        this.fancyAdvancementToastsEnabled = fancyAdvancementToastsEnabled;
        this.fancyQuestToastsEnabled = fancyQuestToastsEnabled;

        this.typesToIgnore.putAll(typesToIgnore);
        this.toastsToIgnore.addAll(toastsToIgnore);
        this.questTypesToIgnore.putAll(questTypesToIgnore);
        for (String toastToIgnore : this.toastsToIgnore) {
            if (toastToIgnore.endsWith("/...")) {
                prefixMatches.add(toastToIgnore.replace("/...", ""));
            } else {
                exactMatches.add(toastToIgnore);
            }
        }
    }

        public ToastsFilteringData() {
        this(true, true, Map.of(
            FancyToastType.TASK, false,
            FancyToastType.GOAL, false,
            FancyToastType.CHALLENGE, false
        ), new ArrayList<>(), Map.of(
            FancyQuestType.BOOK, false,
            FancyQuestType.CHAPTER, false,
            FancyQuestType.QUEST, false,
            FancyQuestType.TASK, false
        ));
        }

    public boolean isTypeIgnored(FancyToastType type) {
        return typesToIgnore.get(type);
    }

    public boolean isQuestTypeIgnored(FancyQuestType key) {
        return questTypesToIgnore.getOrDefault(key, false);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToastsFilteringData that)) return false;
        return fancyAdvancementToastsEnabled == that.fancyAdvancementToastsEnabled && fancyQuestToastsEnabled == that.fancyQuestToastsEnabled && Objects.equals(typesToIgnore, that.typesToIgnore) && Objects.equals(questTypesToIgnore, that.questTypesToIgnore) && Objects.equals(toastsToIgnore, that.toastsToIgnore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, typesToIgnore, questTypesToIgnore, toastsToIgnore);
    }

    @Override
    public int getLatestVersion() {
        return VERSION;
    }

    @Override
    public ToastsFilteringData copy() {
        return new ToastsFilteringData(fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, typesToIgnore, toastsToIgnore, questTypesToIgnore).withLatestVersion();
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "fancyAdvancementToastsEnabled='%s', fancyQuestsToastsEnabled='%s', typesToIgnore='%s', questTypesToIgnore='%s', toastsToIgnore='%s'}",
                fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, typesToIgnore, questTypesToIgnore, toastsToIgnore
        );
    }
}
