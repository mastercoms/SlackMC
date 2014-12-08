package us.circuitsoft.slack.api;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

import static us.circuitsoft.slack.bungee.SlackBungee.getWebhook;

/**
 * Posts a message to Slack when using Bungee.
 */
public class BungeePoster implements Runnable {

    private final String p;
    private final String m;
    private final String w = getWebhook();
    private final String i;

    /**
     * Prepares the message to send to Slack.
     * @param m The message to send to Slack.
     * @param p The username of the message to send to Slack.
     * @param i The image URL of the user that sends the message to Slack. Make this null if the username is a Minecraft player name.
     */
    public BungeePoster (String m, String p, String i) {
        this.p = p;
        this.m = m;
        this.i = i;
    }
            
    @Override
    public void run() {
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
