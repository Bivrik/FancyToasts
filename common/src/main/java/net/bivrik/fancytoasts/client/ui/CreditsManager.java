package net.bivrik.fancytoasts.client.ui;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.config.JsonHelper;
import net.bivrik.fancytoasts.utility.file.Paths;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditsManager {
    private static final String DROPBOX_CREDITS_URL = "https://dl.dropboxusercontent.com/scl/fi/0tupbdajo54oh29617kde/credits.json?rlkey=iro2kolfqyqg6h93gpws45h9s&st=is4wjcmi";

    private CreditsData credits;

    public CreditsData getCredits() {
        return credits;
    }

    public void loadCredits() {
        credits = readCredits();

        if (credits == null) {
            credits = getFallback();
        }

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
        } catch (Exception e) {
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

        Debug.warn("Failed to get credits");
        return null;
    }

    public CreditsData getFallback() {
        Debug.warn("Returning fallback for credits");
        CreditsData data = new CreditsData(new HashMap<>());

        data.addCategory("fallback", users(
                user("Credits couldn't be reached", ":("),
                user("Try restarting mod or just... don't look here yet"),
                user(":/")
        ));

        return data;
    }

    public record CreditsData(Map<String, List<User>> categories) {
        public record User(String name, String annotation) {}

        public void addCategory(String category, List<User> users) {
            categories.put(category, users);
        }
    }

    /**
     * Creates Json file for credits to be then send to DropBox server. Only for development.
     */
    public void createCredits() {
        CreditsData data = new CreditsData(new HashMap<>());

        data.addCategory("boosters", users(
                user("Halil Han", "First booster!")
        ));

        data.addCategory("translators", users(
                user("Gao Xinyang", "Chinese (zh_cn)"),
                user("ChaTian", "Chinese (zh_tw"),
                user("PExPE3", "Japanese (ja_jp)")
        ));

        data.addCategory("github_activists", users(
                user("VaporeonScripts", "<3"),
                user("Mysticpasta1"),
                user("PoIyframeX"),
                user("aisukuma"),
                user("ZakoFish"),
                user("pupcakie"),
                user("guguz"),
                user("TCK-MODDER"),
                user("Redls07"),
                user("HalilMan", "Important bug finder"),
                user("LiterallyLink"),
                user("teenecks"),
                user("F0rsakenPhant0M"),
                user("YnwLNE", "A truck with ideas"),
                user("Memory_Yzf")
        ));

        data.addCategory("special_thanks", users(
                user("Dexpit"),
                user("Starfirexx"),
                user("{user.name}", "<3")
        ));

        JsonHelper.tryToWrite(new File(Paths.CONFIG + "dev_credits.json"), data);
    }

    private CreditsData.User user(String name, String annotation) {
        return new CreditsData.User(name, annotation);
    }

    private CreditsData.User user(String name) {
        return new CreditsData.User(name, null);
    }

    private List<CreditsData.User> users(CreditsData.User... users) {
        return Arrays.asList(users);
    }
}
