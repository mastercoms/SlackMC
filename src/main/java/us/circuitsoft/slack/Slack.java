package us.circuitsoft.slack;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
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

    @Override
    public void onEnable() {
        getLogger().info("Slack has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Slack has been disabled!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String m = event.getMessage();
        post(m, event.getPlayer().getName());
    }
    
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        String m = "logged in";
        post(m, event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String m = "logged out";
        post(m, event.getPlayer().getName());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String m = event.getMessage();
        post(m, event.getPlayer().getName());
    }

    public void post(String m, String p) {
        try {
            String surl = getConfig().getString("webhook");
            URL url = new URL(null);
            if (surl.equals("https://hooks.slack.com/services/")) {
                getLogger().severe("You have not set your webhook URL in the config!");
            } else {
                url = new URL(surl);
            }
            JSONObject j = new JSONObject();
            j.put("text", m);
            j.put("username", p);
            j.put("icon_url", "https://minotar.net/avatar/" + p + "/100.png");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());
            String body = "payload=" + j.toJSONString();
            bos.write(body.getBytes("utf8"));
            bos.flush();
            bos.close();
            int icode = conn.getResponseCode();
            String opcode = Integer.toString(icode);
            String code = conn.getResponseMessage();
            getLogger().info(opcode + code);
            conn.disconnect();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage());
        }
    }
}
