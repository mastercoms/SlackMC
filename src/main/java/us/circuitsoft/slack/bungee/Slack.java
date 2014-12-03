package us.circuitsoft.slack.bungee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Slack extends Plugin implements Listener {

    private boolean n;
    Configuration con;

    @Override
    public void onEnable() {
        con = null;
        getLogger().info("Not supported yet!");
        getProxy().getPluginManager().registerListener(this,this);
        try {
            con = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            getLogger().severe("config.yml does not exist!");
        }
        updateConfig("1.3.0");
        n = con.getString("webhook").equals("https://hooks.slack.com/services/");
        if (n || con.getString("webhook") == null) {
            getLogger().severe("You have not set your webhook URL in the config!");
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onChat(ChatEvent event) {
        ProxiedPlayer p = (ProxiedPlayer) event.getSender();
        String m = event.getMessage();
        getLogger().info("Not supported yet!");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onJoin(ServerConnectedEvent event) {
        ProxiedPlayer p = (ProxiedPlayer) event.getPlayer();
        getLogger().info("Not supported yet!");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer p = (ProxiedPlayer) event.getPlayer();
        getLogger().info("Not supported yet!");
    }

    private boolean blacklist(String m) {
        return !con.getStringList("blacklist").contains(m);
    }

    private void updateConfig(String v) {
        if (!getDataFolder().exists())
                getDataFolder().mkdir();
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
}
