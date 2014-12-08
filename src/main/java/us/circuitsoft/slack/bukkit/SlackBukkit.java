package us.circuitsoft.slack.bukkit;

import java.util.List;
import org.bukkit.ChatColor;
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

public class SlackBukkit extends JavaPlugin implements Listener {

    private boolean n;
    private static String w;
    private List<String> bl;

    @Override
    public void onEnable() {
        getLogger().info("Slack has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        updateConfig(this.getDescription().getVersion());
        w = getConfig().getString("webhook");
        bl = getConfig().getStringList("blacklist");
        n = w.equals("https://hooks.slack.com/services/");
        if (n || w == null) {
            getLogger().severe("You have not set your webhook URL in the config!");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Slack has been disabled!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if (permCheck("slack.hide.chat", event.getPlayer())) {
            send('"' + event.getMessage() + '"', event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerJoinEvent event) {
        if (permCheck("slack.hide.login", event.getPlayer())) {
            send("logged in", event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if (permCheck("slack.hide.logout", event.getPlayer())) {
            send("logged out", event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (blacklist(event.getMessage()) && permCheck("slack.hide.command", event.getPlayer()) && !event.getMessage().contains("/slack send")) {
            send(event.getMessage(), event.getPlayer().getName());
        }
    }

    public void send(String m, String p) {
        new SlackBukkitPoster(this, m, p, null).runTaskAsynchronously(this);
    }

    public void send(String m, String p, String i) {
        new SlackBukkitPoster(this, m, p, i).runTaskAsynchronously(this);
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
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "/slack send <username> <image URL> <message> - send a custom message to slack \n/slack reload - reload Slack's config");
                return true;
            }
            if (args[0].equals("reload")) {
                this.reloadConfig();
                w = getConfig().getString("webhook");
                bl = getConfig().getStringList("blacklist");
                sender.sendMessage(ChatColor.GREEN + "Slack has been reloaded.");
                if (sender.getName() == null ? getServer().getConsoleSender().getName() != null : !sender.getName().equals(getServer().getConsoleSender().getName())) {
                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Slack has been reloaded.");
                }
                return true;
            } else if (args[0].equals("send") && args.length <= 3) {
                sender.sendMessage(ChatColor.GOLD + "/slack send <username> <image URL> <message>");
                return true;
            } else if (args[0].equals("send") && args.length >= 4) {
                String m = "";
                for (int i = 3; i < args.length; i++) {
                    m = m + args[i] + " ";
                }
                send(m, args[1], args[2]);
                return true;
            }
        }
        return false;
    }

    public static String getWebhook() {
        return w;
    }
}
