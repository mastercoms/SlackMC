package org.circuitsoft.slack.api;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

import static org.circuitsoft.slack.bungee.SlackBungee.getWebhookUrl;

/**
 * Posts a message to Slack when using Bungee.
 */
public class BungeePoster implements Runnable {

    private final String name;
    private final String message;
    private final String webhookUrl = getWebhookUrl();
    private final String icon;

    /**
     * Prepares the message to send to Slack.
     *
     * @param message The message to send to Slack.
     * @param name    The username of the message to send to Slack.
     * @param icon    The image URL of the user that sends the message to Slack. Make this null if the username is a Minecraft player name.
     */
    public BungeePoster(String message, String name, String icon) {
        this.name = name;
        this.message = message;
        this.icon = icon;
    }

    @Override
    public void run() {
        JsonObject json = new JsonObject();
        json.addProperty("text", name + ": " + message);
        json.addProperty("username", name);
        if (icon == null) {
            json.addProperty("icon_url", "https://cravatar.eu/helmhead/" + name + "/100.png");
        } else {
            json.addProperty("icon_url", icon);
        }
        try {
            HttpURLConnection webhookConnection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            webhookConnection.setRequestMethod("POST");
            webhookConnection.setDoOutput(true);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream())) {
                String jsonStr = "payload=" + json.toString();
                bufOut.write(jsonStr.getBytes("utf8"));
                bufOut.flush();
            }
            webhookConnection.disconnect();
        } catch (Exception ignored) {
        }
    }
}
