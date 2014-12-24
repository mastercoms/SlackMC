package org.circuitsoft.slack.api;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

import static org.circuitsoft.slack.bukkit.SlackBukkit.getWebhookUrl;
import org.json.simple.JSONObject;

/**
 * Posts a message to Slack when using Bukkit.
 */
public class BukkitPoster extends BukkitRunnable {

    private final String message;
    private final String name;
    private final String iconUrl;
    private final boolean useMarkdown;
    private final String webhookUrl = getWebhookUrl();

    /**
     * Posts a message to Slack.
     *
     * @param message The message sent to Slack.
     * @param name The name attributed to the message sent to Slack.
     * @param iconUrl The image URL of the user that sends the message to Slack.
     * @param useMarkdown Use markdown formatting in the message.
     */
    public BukkitPoster(String message, String name, String iconUrl, boolean useMarkdown) {
        this.message = message;
        this.name = name;
        this.useMarkdown = useMarkdown;
        this.iconUrl = iconUrl;
    }
    
     /**
     * Posts a player sent message to Slack.
     *
     * @param message The message sent to Slack.
     * @param player The player that sent the message.
     * @param useMarkdown Use markdown formatting in the message.
     */
    public BukkitPoster(String message, Player player, boolean useMarkdown) {
        this.message = message;
        name = player.getName();
        iconUrl = "https://cravatar.eu/helmhead/" + player.getUniqueId().toString() + "/128.png";
        this.useMarkdown = useMarkdown;
    }


    @Override
    public void run() {
        JSONObject json = new JSONObject();
        json.put("text", message);
        json.put("username", name);
        json.put("icon_url", iconUrl);
        json.put("mrkdwn", useMarkdown);
        String jsonStr = "payload=" + json.toJSONString();
        try {
            HttpURLConnection webhookConnection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            webhookConnection.setRequestMethod("POST");
            webhookConnection.setDoOutput(true);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream())) {
                bufOut.write(jsonStr.getBytes(StandardCharsets.UTF_8));
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
