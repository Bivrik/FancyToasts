package net.bivrik.fancytoasts.compat;

import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.bivrik.fancytoasts.platform.utility.FancyAdvancementType;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class QuestlogCompat {
    private static final Component ANNOUNCEMENT = Component.translatable("questlog.toast.quest_completed");
    // HOLY SPAM OF REFLECTION
    // But I ain't changing Loom to 1.15, and I don't
    // know gradle that good, and I also didn't sleep
    // for ages already I don't understand
    // what am I even typing at this point.
    // Just change it as soon as possible, it's like a draft version
    // TODO: reduce reflection, override sounds
    private static final Class<?> TOAST_CLASS;
    private static final Class<?> ITEM_RENDERABLE_CLASS;
    private static final Field DISPLAY_DATA_FIELD;
    private static final Method GET_ITEM_METHOD;
    private static final Method GET_ICON_METHOD;
    private static final Method GET_TITLE_METHOD;
    private static final Method GET_DESCRIPTION_METHOD;

    static {
        Class<?> toast = null;
        Class<?> renderable = null;
        Field displayField = null;
        Method getItem = null;
        Method getIcon = null;
        Method getTitle = null;
        Method getDescription = null;

        try {
            toast = Class.forName("org.infernalstudios.questlog.client.gui.components.toasts.QuestCompletedToast");
            Class<?> display = Class.forName("org.infernalstudios.questlog.core.quests.display.QuestDisplayData");
            renderable = Class.forName("org.infernalstudios.questlog.util.texture.ItemRenderable");

            Field f = toast.getDeclaredField("displayData");
            f.setAccessible(true);
            displayField = f;

            Method m = renderable.getDeclaredMethod("getItem");
            m.setAccessible(true);
            getItem = m;

            getIcon = display.getDeclaredMethod("getIcon");
            getIcon.setAccessible(true);
            getTitle = display.getDeclaredMethod("getTitle");
            getTitle.setAccessible(true);
            getDescription = display.getDeclaredMethod("getDescription");
            getDescription.setAccessible(true);

        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            Debug.warn("Questlog classes not found", e);
        }

        TOAST_CLASS = toast;
        ITEM_RENDERABLE_CLASS = renderable;
        DISPLAY_DATA_FIELD = displayField;
        GET_ITEM_METHOD = getItem;
        GET_ICON_METHOD = getIcon;
        GET_TITLE_METHOD = getTitle;
        GET_DESCRIPTION_METHOD = getDescription;
    }

    public static boolean isQuest(Toast toast) {
        if (TOAST_CLASS == null) return false;
        return TOAST_CLASS.isInstance(toast);
    }

    public static AdvancementDisplay getDisplay(Toast toast) {
        if (TOAST_CLASS == null || DISPLAY_DATA_FIELD == null) {
            return null;
        }

        Object displayData;
        try {
            displayData = DISPLAY_DATA_FIELD.get(toast);
        } catch (IllegalAccessException e) {
            Debug.warn("Failed to read displayData field", e);
            return null;
        }

        if (displayData == null) {
            return null;
        }

        ItemStack icon = Items.KNOWLEDGE_BOOK.getDefaultInstance();
        if (GET_ICON_METHOD != null && GET_ITEM_METHOD != null) {
            try {
                Object iconObject = GET_ICON_METHOD.invoke(displayData);
                if (ITEM_RENDERABLE_CLASS != null && ITEM_RENDERABLE_CLASS.isInstance(iconObject)) {
                    Object stack = GET_ITEM_METHOD.invoke(iconObject);
                    if (stack instanceof ItemStack) {
                        icon = (ItemStack) stack;
                    }
                }
            } catch (Exception e) {
                Debug.warn("Failed to get icon from display data", e);
            }
        }

        Component title = Component.empty();
        Component description = Component.empty();
        try {
            if (GET_TITLE_METHOD != null) {
                Object titleObject = GET_TITLE_METHOD.invoke(displayData);
                if (titleObject instanceof Component) title = (Component) titleObject;
            }
            if (GET_DESCRIPTION_METHOD != null) {
                Object descriptionObject = GET_DESCRIPTION_METHOD.invoke(displayData);
                if (descriptionObject instanceof Component) description = (Component) descriptionObject;
            }
        } catch (Exception e) {
            Debug.warn("Failed to get title/description", e);
        }

        FancyAdvancementType type = FancyAdvancementType.GOAL;
        return new AdvancementDisplay(icon, title, description, ANNOUNCEMENT,
                type.getTitleColor(), type.getDescriptionColor(), type.getConventionalType());
    }
}