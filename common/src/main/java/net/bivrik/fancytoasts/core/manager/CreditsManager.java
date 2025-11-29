package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.client.config.JsonHelper;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.IManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CreditsManager implements IManager {
    private static final String DROPBOX_CREDITS_URL = "https://dl.dropboxusercontent.com/scl/fi/0tupbdajo54oh29617kde/credits.json?rlkey=iro2kolfqyqg6h93gpws45h9s&st=is4wjcmi";

    private CreditsData cachedCredits;

    @Override
    public void onModInit() {
        CompletableFuture<CreditsData> creditsData = CompletableFuture.supplyAsync(this::readCredits);
        try {
            cachedCredits = creditsData.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load credits data through wifi connection, using DropBox link", e);
        }

        if (cachedCredits == null) {
            cachedCredits = getFallback();
        }
    }

    public CreditsData getCredits() {
        if (cachedCredits == null) {
            onModInit();
        }

        return cachedCredits;
    }

    private CreditsData readCredits() {
        HttpResponse<String> httpResponse;
        try (HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10L))
                .build()) {

            HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(DROPBOX_CREDITS_URL))
                    .header("User-Agent", "FancyToasts")
                    .header("Cache-Control", "no-cache")
                    .build();

            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            Debug.error("Failed to access DropBox file: {}", e.getMessage());
            return null;
        }

        try {
            if (httpResponse.statusCode() == 200) {
                String response = httpResponse.body();
                if (response == null) {
                    Debug.warn("Response's body is empty, status: {}", httpResponse.statusCode());
                    return null;
                }
                return JsonHelper.getGson().fromJson(response, CreditsData.class);
            }
            else {
                Debug.warn("Status: {}", httpResponse.statusCode());
                Debug.error("Failed to read response's body");
            }
        } catch (Exception e) {
            Debug.error("Failed to get a response from DropBox: {}", e.getMessage());
        }

        Debug.error("Failed to get credits");
        return null;
    }

    public CreditsData getFallback() {
        Debug.warn("Returning fallback for credits");
        CreditsData data = new CreditsData(new HashMap<>());

        data.addCategory("credits_fallback", data.users(
                data.user("Credits couldn't be reached", ":("),
                data.user("Try restarting mod or just... don't look here yet", ":/")
        ));

        return data;
    }

    public record CreditsData(Map<String, List<User>> categories) {
        public record User(String name, String annotation) {}

        public void addCategory(String category, List<User> users) {
            categories.put(category, users);
        }

        public CreditsData.User user(String name, String annotation) {
            return new CreditsData.User(name, annotation);
        }

        public CreditsData.User user(String name) {
            return new CreditsData.User(name, null);
        }

        public List<CreditsData.User> users(CreditsData.User... users) {
            return Arrays.asList(users);
        }
    }
}
