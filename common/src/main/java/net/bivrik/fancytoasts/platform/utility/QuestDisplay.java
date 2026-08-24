package net.bivrik.fancytoasts.platform.utility;

import java.util.List;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class QuestDisplay extends AdvancementDisplay {
    private final List<ItemStack> icons;
    private final QuestType questType;

    public QuestDisplay(List<ItemStack> icons, Component title, Component description, Component announcement, Color titleColor, Color descriptionColor, AdvancementType type, QuestType questType) {
        super(ItemStack.EMPTY, title, description, announcement, titleColor, descriptionColor, type);

        this.icons = icons;
        this.questType = questType;
    }

    public QuestType getQuestType() {
        return questType;
    }

    @Override
    public ItemStack getIcon() {
        if (icons.size() == 1) {
            return icons.get(0);
        } else if (!icons.isEmpty()) {
            return getOrderedIcon();
        }

        return super.getIcon();
    }

    private ItemStack getOrderedIcon() {
        return icons.get((int) (System.currentTimeMillis() / 1000L % icons.size()));
    }
}
