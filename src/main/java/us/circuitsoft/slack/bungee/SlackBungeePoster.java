package us.circuitsoft.slack.bungee;

import com.google.gson.JsonObject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import static us.circuitsoft.slack.bungee.SlackBungee.getWebhook;

/**
 * Poster for Slack plugin's internal use. Do not use this.
 */
public class SlackBungeePoster implements Runnable {

    private final Plugin plugin;
    private final Configuration con;
    private final String p;
    private final String m;
    private final String w = getWebhook();
    private final String i;

    public SlackBungeePoster(Plugin plugin, Configuration con, String m, String p, String i) {
        this.plugin = plugin;
        this.con = con;
        this.p = p;
        this.m = m;
        this.i = i;
    }

    @Override
    public void run() {
        int res;
        JsonObject j = new JsonObject();
        j.addProperty("text", p + ": " + m);
        j.addProperty("username", p);
        if (i == null) {
            j.addProperty("icon_url", "https://cravatar.eu/helmhead/" + p + "/100.png");
        } else {
            j.addProperty("icon_url", i);
        }
        String b = "payload=" + j.toString();
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
            if (con.getBoolean("debug")) {
                plugin.getLogger().log(Level.INFO, "{0} {1}", new Object[]{o, c});
            }
        } catch (MalformedURLException e) {
            plugin.getLogger().log(Level.SEVERE, "URL is not valid: ", e);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "IO exception: ", e);
        }
    }
}
