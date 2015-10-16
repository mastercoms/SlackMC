package org.circuitsoft.slackmc.api.bukkit;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static org.circuitsoft.slackmc.api.bukkit.SlackBukkit.getSlackPluginApi;

public class SlackSenderApi {

    private SlackApi slackApi;
    private JavaPlugin plugin;

    protected SlackSenderApi(final JavaPlugin plugin, final String webhook) {
        slackApi = new SlackApi(webhook);
        this.plugin = plugin;
    }

    /**
     * Send a message as a player.
     *
     * @param player  The player to send the message as.
     * @param message The message to send as the player. The username and icon will be overridden.
     */
    public void sendAsPlayer(final Player player, final SlackMessage message) {
        if (getSlackPluginApi().getRegisteredPlugins().contains(plugin)) {
            message.setUsername(player.getName());
            message.setIcon("https://mcapi.ca/avatar/3d/" + player.getName());
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new SlackSendThread(message));
        }
    }

    /**
     * Send a message as a plugin.
     *
     * @param message The message to send as the plugin. The username will be overridden.
     */
    public void sendAsPlugin(final SlackMessage message) {
        if (getSlackPluginApi().getRegisteredPlugins().contains(plugin)) {
            message.setUsername(plugin.getDescription().getName());
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new SlackSendThread(message));
        }
    }

    /**
     * Send a custom message.
     *
     * @param message The message to send.
     */
    public void sendAsCustom(final SlackMessage message) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new SlackSendThread(message));
    }

    private final class SlackSendThread extends BukkitRunnable {

        private SlackMessage message;

        private SlackSendThread(final SlackMessage message) {
            this.message = message;
        }

        @Override
        public void run() {
            slackApi.call(message);
        }
    }
}
