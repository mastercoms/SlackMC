package us.circuitsoft.slack;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

public class Slack extends JavaPlugin implements Listener {

    private boolean setWebhook;

    @Override
    public void onEnable() {
        getLogger().info("Slack has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        setWebhook = !getConfig().getString("webhook").equals("https://hooks.slack.com/services/");
        if (!setWebhook) {
            getLogger().severe("You have not set your webhook URL in the config!");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Slack has been disabled!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        payload('"' + event.getMessage() + '"', event.getPlayer().getName());
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        payload("logged in", event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        payload("logged out", event.getPlayer().getName());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        payload(event.getMessage(), event.getPlayer().getName());
    }

    public void payload(String m, String p) {
        JSONObject j = new JSONObject();
        j.put("text", m);
        j.put("username", p);
        j.put("icon_url", "https://minotar.net/avatar/" + p + "/100.png");
        String b = "payload=" + j.toJSONString();
        post(b);
    }

    public void post(String b) {
        if (!setWebhook) {
            getLogger().severe("You have not set your webhook URL in the config!");
        } else {
            try {
                URL u = new URL(getConfig().getString("webhook"));
                HttpURLConnection C = (HttpURLConnection)u.openConnection();
                C.setRequestMethod("POST");
                C.setDoOutput(true);
                try (BufferedOutputStream B = new BufferedOutputStream(C.getOutputStream())) {
                    B.write(b.getBytes("utf8"));
                    B.flush();
                }
                int i = C.getResponseCode();
                String o = Integer.toString(i);
                String c = C.getResponseMessage();
                getLogger().log(Level.INFO, "{0} {1}", new Object[]{o, c});
                C.disconnect();
                } catch (MalformedURLException e) {
                    getLogger().log(Level.SEVERE, "URL is not valid: ", e);
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "IO exception: ", e);
                }
        }
    }
}
