package net.bivrik.fancytoasts.compat;

import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.bivrik.fancytoasts.platform.utility.FancyAdvancementType;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.infernalstudios.questlog.client.gui.components.toasts.QuestCompletedToast;
import org.infernalstudios.questlog.core.quests.display.QuestDisplayData;
import org.infernalstudios.questlog.util.texture.ItemRenderable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QuestlogCompat {
    private static final Component ANNOUNCEMENT = Component.translatable("questlog.toast.quest_completed");
    // Holy spam of reflection :sob:
    private static final Field DISPLAY_DATA;
    private static final Method GET_ITEM;

    static {
        Field field = null;
        Method method = null;

        try {
            field = QuestCompletedToast.class.getDeclaredField("displayData");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Debug.warn("No field displayData in QuestCompletedToast from Questlog", e);
        }

        try {
            method = ItemRenderable.class.getDeclaredMethod("getItem");
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Debug.warn("No method getItem in ItemRenderable from Questlog", e);
        }

        DISPLAY_DATA = field;
        GET_ITEM = method;
    }

    public static boolean isQuest(Toast toast) {
        return toast instanceof QuestCompletedToast;
    }

    // I do not want to add freaking mixins to this
    public static AdvancementDisplay getDisplay(Toast toast) {
        if (DISPLAY_DATA == null) {
            return null;
        }

        QuestDisplayData displayData;
        try {
            displayData = (QuestDisplayData) DISPLAY_DATA.get((QuestCompletedToast) toast);
        } catch (IllegalAccessException e) {
            Debug.warn("Failed to access displayData from QuestCompletedToast from Questlog", e);
            return null;
        }

        if (displayData == null) {
            return null;
        }

        ItemStack icon = null;
        if (GET_ITEM != null && displayData.getIcon() instanceof ItemRenderable itemRenderable) {
            try {
                icon = (ItemStack) GET_ITEM.invoke(itemRenderable);
            } catch (IllegalAccessException e) {
                Debug.warn("Failed to access getItem from QuestCompletedToast from Questlog", e);
            } catch (InvocationTargetException e) {
                Debug.warn("Failed to invoke getItem from QuestCompletedToast from Questlog", e);
            }
        }

        if (icon == null || icon.isEmpty()) {
            icon = Items.KNOWLEDGE_BOOK.getDefaultInstance();
        }

        FancyAdvancementType type = FancyAdvancementType.GOAL;
        return new AdvancementDisplay(icon, displayData.getTitle(), displayData.getDescription(), ANNOUNCEMENT,
                type.getTitleColor(), type.getDescriptionColor(), type.getConventionalType());
    }
}
