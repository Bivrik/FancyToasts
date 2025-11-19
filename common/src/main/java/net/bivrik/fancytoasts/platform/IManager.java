package net.bivrik.fancytoasts.platform;

import net.minecraft.client.Minecraft;

public interface IManager {
    default void onModInit() {}
    default void onMinecraftInit(Minecraft minecraft) {}
}
