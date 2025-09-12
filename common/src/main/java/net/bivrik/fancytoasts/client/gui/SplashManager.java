package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SplashManager {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "splashes.txt");
    private static final Random rnd = new Random();

    private final User user;

    private List<String> splashes;

    public SplashManager(User user) {
        this.user = user;
    }

    public void load(ResourceManager resourceManager) {
        try {
            Optional<Resource> resource = resourceManager.getResource(LOCATION);
            if (resource.isPresent()) {
                BufferedReader reader = new BufferedReader(resource.get().openAsReader());
                splashes = reader.lines().toList();
            }
            else {
                Debug.error("Could not start reading, because it is not exist");
            }
        } catch (IOException e) {
            Debug.error("Could not read or get splash file. Error: {}", e);
        }
    }

    public String getSplash() {
        if (!splashes.isEmpty()) {
            String splash = splashes.get(rnd.nextInt(splashes.size()));

            if (splash.contains("{user.name}")) {
                splash = splash.replace("{user.name}", user.getName());
            }

            return splash;
        }

        Debug.error("Could not get splash, it is empty");
        return "";
    }
}
