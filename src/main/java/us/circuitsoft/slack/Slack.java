package us.circuitsoft.slack;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Slack extends JavaPlugin implements Listener {

    private boolean n;
    private static String w;
    private List<String> bl;

    @Override
    public void onEnable() {
        getLogger().info("Slack has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        updateConfig("1.3.0");
        w = getConfig().getString("webhook");
        bl = getConfig().getStringList("blacklist");
        n = w.equals("https://hooks.slack.com/services/");
        if (n) {
            getLogger().severe("You have not set your webhook URL in the config!");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Slack has been disabled!");
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if (permCheck("slack.hide.chat", event.getPlayer())) {
            payload('"' + event.getMessage() + '"', event.getPlayer().getName());
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onLogin(PlayerJoinEvent event) {
        if(permCheck("slack.hide.login", event.getPlayer())) {
            payload("logged in", event.getPlayer().getName());
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if(permCheck("slack.hide.logout", event.getPlayer())) {
            payload("logged out", event.getPlayer().getName());
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (blacklist(event.getMessage()) && permCheck("slack.hide.command", event.getPlayer())) {
            payload(event.getMessage(), event.getPlayer().getName());
        }
    }

    public void payload(String m, String p) {
           new Poster(this, m, p, w, null).runTaskAsynchronously(this);
    }

    public void payload(String m, String p, String i) {
           new Poster(this, m, p, w, i).runTaskAsynchronously(this);
    }

    private boolean blacklist(String m) {
        if (getConfig().getBoolean("use-blacklist")) {
            return !bl.contains(m);
        } else {
            return true;
        }
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
        if (getConfig().getBoolean("use-perms")) {
            return !p.hasPermission(c);
        } else {
            return true;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("slack")) {
            this.reloadConfig();
            w = getConfig().getString("webhook");
            bl = getConfig().getStringList("blacklist");
            sender.sendMessage("Slack has been reloaded.");
            if (sender.getName() == null ? getServer().getConsoleSender().getName() != null : !sender.getName().equals(getServer().getConsoleSender().getName())) {
                getServer().getConsoleSender().sendMessage("Slack has been reloaded.");
            }
            return true;
        }
        return false;
    }
    
}
