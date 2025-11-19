package net.bivrik.fancytoasts.client.ui;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.platform.IManager;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SplashManager implements IManager {
    private static final Logger LOGGER = Debug.getLogger(SplashManager.class);

    private static final ResourceLocation LOCATION = ResourceLocations.of("splashes.txt");
    private static final Random RANDOM = new Random();

    private User user;
    private List<String> splashes;

    @Override
    public void onMinecraftInit(Minecraft minecraft) {
        user = minecraft.getUser();

        readSplashes(minecraft.getResourceManager());
    }

    private void readSplashes(ResourceManager resourceManager) {
        try {
            Optional<Resource> resource = resourceManager.getResource(LOCATION);
            if (resource.isPresent()) {
                BufferedReader reader = new BufferedReader(resource.get().openAsReader());
                splashes = reader.lines().toList();
            }
            else {
                LOGGER.error("Could not start reading, because it does not exist");
            }
        } catch (IOException e) {
            LOGGER.error("Could not access splash file: {}", e.getMessage());
        }
    }

    public String getSplash() {
        if (!splashes.isEmpty()) {
            String splash = splashes.get(RANDOM.nextInt(splashes.size()));

            if (splash.contains("{user.name}")) {
                splash = splash.replace("{user.name}", user.getName());
            }

            return splash;
        }

        LOGGER.error("Could not get splash, it is empty");
        return "";
    }
}
