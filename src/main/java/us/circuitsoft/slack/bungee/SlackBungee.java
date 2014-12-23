package us.circuitsoft.slack.bungee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
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

    private static String webhookUrl;
    private List<String> blacklist;
    private Configuration config;

    @Override
    public void onEnable() {
        getLogger().info("Slack has been enabled!");
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new SlackBungeeCommand(this));
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "config.yml does not exist: ", ex);
        }
        updateConfig(this.getDescription().getVersion());
        webhookUrl = config.getString("webhook");
        blacklist = config.getStringList("blacklist");
        if (webhookUrl == null || webhookUrl.trim().isEmpty() || webhookUrl.equals("https://hooks.slack.com/services/")) {
            getLogger().severe("You have not set your webhook URL in the config!");
        }
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "config.yml does not exist: ", ex);
        }
        webhookUrl = config.getString("webhook");
        blacklist = config.getStringList("blacklist");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent event) {
        ProxiedPlayer p = (ProxiedPlayer) event.getSender();
        if (event.isCommand()) {
            if (!hasPermission(p, "slack.hide.command") && !isOnBlacklist(event.getMessage())) {
                send('"' + event.getMessage() + '"', p.getName() + " (" + p.getServer().getInfo().getName() + ")");
            }
        } else if (!hasPermission(p, "slack.hide.chat")) {
            send('"' + event.getMessage() + '"', p.getName() + " (" + p.getServer().getInfo().getName() + ")");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(ServerConnectedEvent event) {
        if (!hasPermission(event.getPlayer(), "slack.hide.login")) {
            send("logged in", event.getPlayer().getName() + " (" + event.getServer().getInfo().getName() + ")");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(ServerDisconnectEvent event) {
        if (!hasPermission(event.getPlayer(), "slack.hide.logout")) {
            send("logged out", event.getPlayer().getName() + " (" + event.getTarget().getName() + ")");
        }
    }

    public void send(String message, String name) {
        send(message, name, null);
    }

    public void send(String m, String p, String i) {
        getProxy().getScheduler().runAsync(this, new SlackBungeePoster(this, con, m, p, i));
    }

    private boolean isOnBlacklist(String name) {
        if (config.getBoolean("use-blacklist")) {
            return blacklist.contains(name);
        } else {
            return false;
        }
    }

    private void updateConfig(String version) {
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
        String configV = config.getString("v");
        if (configV == null) {
            if (version != null) {
                config.set("version", version);
            }
        } else {
            if (!configV.equals(version)) {
                config.set("version", version);
            }
        }
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Failed to save config: ", ex);
        }
    }

    private boolean hasPermission(ProxiedPlayer player, String permission) {
        if (config.getBoolean("use-perms")) {
            return player.hasPermission(permission);
        } else {
            return false;
        }
    }

    public static String getWebhookUrl() {
        return webhookUrl;
    }
}
