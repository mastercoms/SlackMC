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
import static us.circuitsoft.slack.bukkit.SlackBukkit.getWebhook;

/**
 * Poster for Slack plugin's internal use. Do not use this.
 */
public class SlackBukkitPoster extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final String p;
    private final String m;
    private final String w = getWebhook();
    private final String i;

    public SlackBukkitPoster(JavaPlugin plugin, String m, String p, String i) {
        this.plugin = plugin;
        this.p = p;
        this.m = m;
        this.i = i;
    }

    @Override
    public void run() {
        int res;
        JSONObject j = new JSONObject();
        j.put("text", p + ": " + m);
        j.put("username", p);
        if (i == null) {
            j.put("icon_url", "https://cravatar.eu/helmhead/" + p + "/100.png");
        } else {
            j.put("icon_url", i);
        }
        String b = "payload=" + j.toJSONString();
        try {
            URL u = new URL(w);
            HttpURLConnection C = (HttpURLConnection) u.openConnection();
            C.setRequestMethod("POST");
            C.setDoOutput(true);
            try (BufferedOutputStream B = new BufferedOutputStream(C.getOutputStream())) {
                B.write(b.getBytes("utf8"));
                B.flush();
            }
            C.disconnect();
            res = C.getResponseCode();
            String o = Integer.toString(res);
            String c = C.getResponseMessage();
            if (plugin.getConfig().getBoolean("debug")) {
                plugin.getLogger().log(Level.INFO, "{0} {1}", new Object[]{o, c});
            }
        } catch (MalformedURLException e) {
            plugin.getLogger().log(Level.SEVERE, "URL is not valid: ", e);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "IO exception: ", e);
        }
    }
}
