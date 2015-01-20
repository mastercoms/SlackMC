package org.circuitsoft.slack.api;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import static org.circuitsoft.slack.bukkit.SlackBukkit.getWebhookUrl;

/**
 * Posts a message to Slack when using Bukkit.
 */
public class BukkitPoster extends BukkitRunnable {

    private final String name;
    private final String message;
    private final String webhookUrl = getWebhookUrl();
    private final String iconUrl;
    private final Boolean isAction;

    /**
     * Prepares the message to send to Slack.
     *
     * @param message The message to send to Slack.
     * @param name    The username of the message to send to Slack.
     * @param iconUrl The image URL of the user that sends the message to Slack. Make this null if the username is a Minecraft player name.
     * @param isAction  The message is an action or not.
     */
    public BukkitPoster(String message, String name, String iconUrl, Boolean isAction) {
        this.name = name;
        this.message = message;
        this.iconUrl = iconUrl;
        this.isAction = isAction;
    }

    /**
     * Prepares the message to send to Slack.
     *
     * @param message The message to send to Slack.
     * @param name    The username of the message to send to Slack.
     * @param iconUrl The image URL of the user that sends the message to Slack. Make this null if the username is a Minecraft player name.
     */
    public BukkitPoster(String message, String name, String iconUrl) {
         this(message, name, iconUrl, false);
    }

    @Override
    public void run() {
        JSONObject json = new JSONObject();
        if (isAction) {
          json.put("text", "_" + message + "_");
        } else if (message.startsWith("/")) {
        	json.put("text", "```" + message + "```");
        } else {
        	json.put("text", message);
        	json.put("mrkdwn", false);
        }
        json.put("username", name);
        if (iconUrl == null) {
            json.put("icon_url", "https://cravatar.eu/helmhead/" + name + "/100.png");
        } else {
            json.put("icon_url", iconUrl);
        }
        String jsonStr = "payload=" + json.toJSONString();
        try {
            HttpURLConnection webhookConnection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            webhookConnection.setRequestMethod("POST");
            webhookConnection.setDoOutput(true);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream())) {
                bufOut.write(jsonStr.getBytes(StandardCharsets.UTF_8));
                bufOut.flush();
            }
            webhookConnection.disconnect();
        } catch (Exception ignored) {
        }
    }
}
