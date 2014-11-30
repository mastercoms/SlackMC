package us.circuitsoft.slack;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

public class Slack extends JavaPlugin implements Listener {

    private boolean n;

    @Override
    public void onEnable() {
        getLogger().info("Slack has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        updateConfig("1.2.0");
        n = getConfig().getString("webhook").equals("https://hooks.slack.com/services/");
        if (n) {
            getLogger().severe("You have not set your webhook URL in the config!");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Slack has been disabled!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (permCheck("slack.hide.chat", event.getPlayer())) {
            payload('"' + event.getMessage() + '"', event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (permCheck("slack.hide.logout", event.getPlayer())) {
            payload("logged in", event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(permCheck("slack.hide.logout", event.getPlayer())) {
            payload("logged out", event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (blacklist(event.getMessage()) && permCheck("slack.hide.command", event.getPlayer())) {
            payload(event.getMessage(), event.getPlayer().getName());
        }
    }

    /**
     * Send a message to Slack.
     * @param m The message sent to Slack.
     * @param p The name of the sender of the message sent to Slack.
     * @return True if the message was successfully sent to Slack.
     */
    public boolean payload(String m, String p) {
        JSONObject j = new JSONObject();
        j.put("text", p + ": " + m);
        j.put("username", p);
        j.put("icon_url", "https://cravatar.eu/helmhead/" + p + "/100.png");
        String b = "payload=" + j.toJSONString();
        return post(b);
    }
    
    /**
     * Send a message to Slack with a custom user icon.
     * @param m The message sent to Slack.
     * @param p The name of the sender of the message sent to Slack.
     * @param i The URL of an image of the sender of the message sent to Slack. (recommended for non player messages).
     * @return True if the message was successfully sent to Slack.
     */
    public boolean payload(String m, String p, String i) {
        if(permCheck("slack.hide.*", getServer().getPlayer(p))) {
            JSONObject j = new JSONObject();
            j.put("text", p + ": " + m);
            j.put("username", p);
            j.put("icon_url", i);
            String b = "payload=" + j.toJSONString();
            return post(b);
        } else {
            return false;
        }
    }

    private boolean post(String b) {
        int i = 0;
        if (n) {
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
                i = C.getResponseCode();
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
        return i == 200;
    }

    private boolean blacklist(String m) {
        return !getConfig().getStringList("blacklist").contains(m);
    }

    private void updateConfig(String v) {
        this.saveDefaultConfig();
        if (getConfig().getString("v") == null ? v != null : !getConfig().getString("v").equals(v)) {
            getConfig().options().copyDefaults(true);
            getConfig().set("version", v);
        }
        this.saveConfig();
    }

    private boolean permCheck(String c, Player p) {
        return !p.hasPermission(c);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("slack")) {
            this.reloadConfig();
            sender.sendMessage("Slack has been reloaded.");
            if (sender.getName() == null ? getServer().getConsoleSender().getName() != null : !sender.getName().equals(getServer().getConsoleSender().getName())) {
                getServer().getConsoleSender().sendMessage("Slack has been reloaded.");
            }
            return true;
        }
        return false;
    }
}
