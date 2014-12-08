package us.circuitsoft.slack.bungee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class SlackBungee extends Plugin implements Listener {

    private boolean n;
    private static String w;
    private List<String> bl;
    Configuration con;

    @Override
    public void onEnable() {
        con = null;
        getLogger().info("Slack has been enabled!");
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new SlackBungeeCommand());
        try {
            con = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "config.yml does not exist: ", ex);
        }
        updateConfig(this.getDescription().getVersion());
        w = con.getString("webhook");
        bl = con.getStringList("blacklist");
        n = w.equals("https://hooks.slack.com/services/");
        if (n || con.getString("webhook") == null) {
            getLogger().severe("You have not set your webhook URL in the config!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent event) {
        ProxiedPlayer p = (ProxiedPlayer) event.getSender();
        if (event.isCommand()) {
            if (permCheck("slack.hide.command", p) && blacklist(event.getMessage())) {
                send('"' + event.getMessage() + '"', p.getName() + " (" + p.getServer().getInfo().getName() + ")");
            }
        } else if (permCheck("slack.hide.chat", p)) {
            send('"' + event.getMessage() + '"', p.getName() + " (" + p.getServer().getInfo().getName() + ")");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(ServerConnectedEvent event) {
        if (permCheck("slack.hide.login", event.getPlayer())) {
            send("logged in", event.getPlayer().getName() + " (" + event.getServer().getInfo().getName() + ")");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(ServerDisconnectEvent event) {
        if (permCheck("slack.hide.logout", event.getPlayer())) {
            send("logged out", event.getPlayer().getName() + " (" + event.getTarget().getName() + ")");
        }
    }

    public void send(String m, String p) {
        Executors.newSingleThreadExecutor().submit(new SlackBungeePoster(this, con, m, p, null));
    }

    public void send(String m, String p, String i) {
        Executors.newSingleThreadExecutor().submit(new SlackBungeePoster(this, con, m, p, i));
    }

    private boolean blacklist(String m) {
        if (con.getBoolean("use-blacklist")) {
            return !bl.contains(m);
        } else {
            return true;
        }
    }

    private void updateConfig(String v) {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                Files.copy(getResourceAsStream("config.yml"), file.toPath());
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, "Default config not saved: ", ex);
            }
        }
        if (con.getString("v") == null ? v != null : !con.getString("v").equals(v)) {
            con.set("version", v);
        }
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(con, new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Failed to save config: ", ex);
        }
    }

    private boolean permCheck(String c, ProxiedPlayer p) {
        return !p.hasPermission(c);
    }

    public static String getWebhook() {
        return w;
    }
}
