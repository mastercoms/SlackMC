package org.circuitsoft.slackmc.api.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Server;

import java.util.logging.Level;

public class SlackReceiveServer extends BukkitRunnable {

    JavaPlugin plugin;

    protected  SlackReceiveServer (JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        final Server server = new Server(8080);
        server.setHandler(new SlackReceiveHandler());
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error in starting SlackMC server: " + e);
        }
    }
}
