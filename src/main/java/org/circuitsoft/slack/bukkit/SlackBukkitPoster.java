package org.circuitsoft.slack.bukkit;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import static org.circuitsoft.slack.bukkit.SlackBukkit.getWebhookUrl;

/**
 * Poster for Slack plugin's internal use. Do not use this.
 */
public class SlackBukkitPoster extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final String name;
    private final String message;
    private final String webhookUrl = getWebhookUrl();
    private final String iconUrl;
    private final Boolean isAction;

    public SlackBukkitPoster(JavaPlugin plugin, String message, String name, String iconUrl) {
        this(plugin, message, name, iconUrl, false);
    }

    public SlackBukkitPoster(JavaPlugin plugin, String message, String name, String iconUrl, Boolean isAction) {
        this.plugin = plugin;
        this.name = name;
        this.message = message;
        this.iconUrl = iconUrl;
        this.isAction = isAction;
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
        String b = "payload=" + json.toJSONString();
        try {
            HttpURLConnection webhookConnection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            webhookConnection.setRequestMethod("POST");
            webhookConnection.setDoOutput(true);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream())) {
                bufOut.write(b.getBytes("utf8"));
                bufOut.flush();
            }
            webhookConnection.disconnect();
            int responseCode = webhookConnection.getResponseCode();
            String responseMessage = webhookConnection.getResponseMessage();
            if (plugin.getConfig().getBoolean("debug")) {
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
