package us.circuitsoft.slack.bukkit;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import static us.circuitsoft.slack.bukkit.SlackBukkit.getWebhookUrl;

/**
 * Poster for Slack plugin's internal use. Do not use this.
 */
public class SlackBukkitPoster extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final String name;
    private final String message;
    private final String webhookUrl = getWebhookUrl();
    private final String iconUrl;

    public SlackBukkitPoster(JavaPlugin plugin, String message, String name, String iconUrl) {
        this.plugin = plugin;
        this.name = name;
        this.message = message;
        this.iconUrl = iconUrl;
    }

    @Override
    public void run() {
        JSONObject json = new JSONObject();
        json.put("text", name + ": " + message);
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
            if (plugin.getConfig().getBoolean("debug")) {
                plugin.getLogger().log(Level.INFO, "{0} {1}", new Object[]{
                        webhookConnection.getResponseCode(),
                        webhookConnection.getResponseMessage()
                });
            }
        } catch (MalformedURLException e) {
            plugin.getLogger().log(Level.SEVERE, "URL is not valid: ", e);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "IO exception: ", e);
        }
    }
}
