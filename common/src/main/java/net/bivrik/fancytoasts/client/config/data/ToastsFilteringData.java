package net.bivrik.fancytoasts.client.config.data;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.QuestType;
import net.bivrik.fancytoasts.platform.utility.FancyAdvancementType;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.resources.ResourceLocation;

public class ToastsFilteringData extends ConfigData {
    private static final int VERSION = Constants.ConfigVersions.TOAST_FILTERING;

    private boolean fancyAdvancementToastsEnabled;
    private boolean fancyQuestToastsEnabled;

    // Only set on load, changing through config file
    private final List<String> toastsToIgnore = new ArrayList<>();
    private final Map<FancyAdvancementType, Boolean> typesToIgnore = new EnumMap<>(FancyAdvancementType.class);
    private final Map<QuestType, Boolean> questTypesToIgnore = new EnumMap<>(QuestType.class);

    private final transient Set<String> exactMatches = new HashSet<>();
    private final transient Set<String> prefixMatches = new HashSet<>();

    private ToastsFilteringData(boolean fancyAdvancementToastsEnabled, boolean fancyQuestToastsEnabled, List<String> toastsToIgnore, Map<FancyAdvancementType, Boolean> typesToIgnore, Map<QuestType, Boolean> questTypesToIgnore) {
        super(Paths.TOASTS_FILTERING_FILE);

        this.fancyAdvancementToastsEnabled = fancyAdvancementToastsEnabled;
        this.fancyQuestToastsEnabled = fancyQuestToastsEnabled;

        this.toastsToIgnore.addAll(toastsToIgnore);
        for (String toastToIgnore : toastsToIgnore) {
            if (toastToIgnore.endsWith("/...")) {
                prefixMatches.add(toastToIgnore.replace("/...", ""));
            } else {
                exactMatches.add(toastToIgnore);
            }
        }
        this.typesToIgnore.putAll(typesToIgnore);
        this.questTypesToIgnore.putAll(questTypesToIgnore);
    }

        public ToastsFilteringData() {
            this(true, true, new ArrayList<>(),
                    Map.of(
                            FancyAdvancementType.TASK, false,
                            FancyAdvancementType.GOAL, false,
                            FancyAdvancementType.CHALLENGE, false),
                    Map.of(
                            QuestType.TASK, false,
                            QuestType.QUEST, false,
                            QuestType.CHAPTER, false,
                            QuestType.BOOK, false)
            );
        }

    public boolean isTypeIgnored(FancyAdvancementType type) {
        return typesToIgnore.get(type);
    }

    public boolean isQuestTypeIgnored(QuestType key) {
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
    public int getLatestVersion() {
        return VERSION;
    }

    @Override
    public ToastsFilteringData copy() {
        return new ToastsFilteringData(fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, toastsToIgnore, typesToIgnore, questTypesToIgnore).withLatestVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(fancyAdvancementToastsEnabled, fancyQuestToastsEnabled, toastsToIgnore, typesToIgnore, questTypesToIgnore);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToastsFilteringData that)) return false;
        return fancyAdvancementToastsEnabled == that.fancyAdvancementToastsEnabled
                && fancyQuestToastsEnabled == that.fancyQuestToastsEnabled
                && Objects.equals(typesToIgnore, that.typesToIgnore)
                && Objects.equals(questTypesToIgnore, that.questTypesToIgnore)
                && Objects.equals(toastsToIgnore, that.toastsToIgnore);
    }

    @Override
    public String toString() {
        return getBaseToStringBuilder()
                .append("fancyAdvancementToastsEnabled", fancyAdvancementToastsEnabled)
                .append("fancyQuestToastsEnabled", fancyQuestToastsEnabled)
                .append("toastsToIgnore", toastsToIgnore)
                .append("typesToIgnore", typesToIgnore)
                .append("questTypesToIgnore", questTypesToIgnore)
                .toString();
    }
}
