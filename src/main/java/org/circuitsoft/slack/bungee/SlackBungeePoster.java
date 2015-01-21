package org.circuitsoft.slack.bungee;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import static org.circuitsoft.slack.bungee.SlackBungee.getWebhookUrl;

/**
 * Poster for Slack plugin's internal use. Do not use this.
 */
public class SlackBungeePoster implements Runnable {

    private final Plugin plugin;
    private final Configuration config;
    private final String name;
    private final String message;
    private final String webhookUrl = getWebhookUrl();
    private final String iconUrl;

    public SlackBungeePoster(Plugin plugin, Configuration config, String message, String name, String iconUrl) {
        this.plugin = plugin;
        this.config = config;
        this.name = name;
        this.message = message;
        this.iconUrl = iconUrl;
    }

    @Override
    public void run() {
        JsonObject json = new JsonObject();
        json.addProperty("text", name + ": " + message);
        json.addProperty("username", name);
        if (iconUrl == null) {
            json.addProperty("icon_url", "https://cravatar.eu/helmhead/" + name + "/100.png");
        } else {
            json.addProperty("icon_url", iconUrl);
        }
        try {
            HttpURLConnection webhookConnection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            webhookConnection.setRequestMethod("POST");
            webhookConnection.setDoOutput(true);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream())) {
                String jsonStr = "payload=" + json.toString();
                bufOut.write(jsonStr.getBytes(StandardCharsets.UTF_8));
                bufOut.flush();
            }
            webhookConnection.disconnect();
            int responseCode = webhookConnection.getResponseCode();
            String responseMessage = webhookConnection.getResponseMessage();
            if (config.getBoolean("debug")) {
                plugin.getLogger().log(Level.INFO, "{0} {1}", new Object[]{
                        responseCode,
                        responseMessage
                });
            }
        } catch (MalformedURLException e) {
            plugin.getLogger().log(Level.SEVERE, "URL is not valid: ", e);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "IO exception: ", e);
        }
    }
}
