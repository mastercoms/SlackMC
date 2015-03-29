package org.circuitsoft.slack.api;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static org.circuitsoft.slack.bungee.SlackBungee.getWebhookUrl;

/**
 * Posts a message to Slack when using Bungee.
 */
public class BungeePoster implements Runnable {
    
    private final String message;
    private final String name;
    private final String iconUrl;
    private final boolean useMarkdown;
    private final String webhookUrl = getWebhookUrl();

    /**
     * Posts a message to Slack involving the proxy or network.
     *
     * @param message The message sent to Slack.
     * @param name The name attributed to the message sent to Slack.
     * @param iconUrl The image URL of the user that sends the message to Slack.
     * @param useMarkdown Use markdown formatting in the message.
     */
    public BungeePoster(String message, String name, String iconUrl, boolean useMarkdown) {
        this.message = message;
        this.name = name;
        this.useMarkdown = useMarkdown;
        this.iconUrl = iconUrl;
    }
    
    /**
     * Posts a message to Slack involving a server on the proxy.
     *
     * @param message The message sent to Slack.
     * @param name The name attributed to the message sent to Slack.
     * @param iconUrl The image URL of the user that sends the message to Slack.
     * @param serverName The server the event took place on.
     * @param useMarkdown Use markdown formatting in the message.
     */
    public BungeePoster(String message, String name, String iconUrl, String serverName, boolean useMarkdown) {
        this.message = message;
        this.name = name + " (" + serverName + ")";
        this.useMarkdown = useMarkdown;
        this.iconUrl = iconUrl;
    }
    
     /**
     * Posts a player sent message to Slack.
     *
     * @param message The message sent to Slack.
     * @param player The player that sent the message.
     * @param serverName The server the player is on.
     * @param useMarkdown Use markdown formatting in the message.
     */
    public BungeePoster(String message, ProxiedPlayer player, String serverName, boolean useMarkdown) {
        this.message = message;
        name = player.getName() + " (" + serverName + ")";
        iconUrl = "https://cravatar.eu/helmhead/" + player.getUniqueId().toString() + "/128.png";
        this.useMarkdown = useMarkdown;
    }

    @Override
    public void run() {
        JsonObject json = new JsonObject();
        json.addProperty("text", message);
        json.addProperty("username", name);
        json.addProperty("icon_url", iconUrl);;
        json.addProperty("mrkdwn", useMarkdown);
        try {
            HttpURLConnection webhookConnection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            webhookConnection.setRequestMethod("POST");
            webhookConnection.setDoOutput(true);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream())) {
                String jsonStr = "payload=" + json.toString();
                bufOut.write(jsonStr.getBytes("utf8"));
                bufOut.flush();
                bufOut.close();
            }
            int serverResponseCode = webhookConnection.getResponseCode();
            webhookConnection.disconnect();
            webhookConnection = null;
        } catch (Exception ignored) {
        }
    }
}
