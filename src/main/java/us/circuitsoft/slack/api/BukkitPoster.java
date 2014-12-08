package us.circuitsoft.slack.api;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import static us.circuitsoft.slack.bukkit.SlackBukkit.getWebhook;

/**
 * Posts a message to Slack when using Bukkit.
 */
public class BukkitPoster extends BukkitRunnable {

    private final String p;
    private final String m;
    private final String w;
    private final String i;

    /**
     * Prepares the message to send to Slack.
     * @param m The message to send to Slack.
     * @param p The username of the message to send to Slack.
     * @param i The image URL of the user that sends the message to Slack. Make this null if the username is a Minecraft player name.
     */
    public BukkitPoster (String m, String p, String i) {
        this.p = p;
        this.m = m;
        this.i = i;
        this.w = getWebhook();
    }
            
    @Override
    public void run() {
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
                HttpURLConnection C = (HttpURLConnection)u.openConnection();
                C.setRequestMethod("POST");
                C.setDoOutput(true);
                try (BufferedOutputStream B = new BufferedOutputStream(C.getOutputStream())) {
                    B.write(b.getBytes("utf8"));
                    B.flush();
                }
                C.disconnect();
                } catch (Exception e) {}
    }
}
