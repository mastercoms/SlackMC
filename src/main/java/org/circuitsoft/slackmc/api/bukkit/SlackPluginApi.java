package org.circuitsoft.slackmc.api.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class SlackPluginApi {

    private ArrayList<JavaPlugin> registeredPlugins;

    protected SlackPluginApi() {
        registeredPlugins = new ArrayList<>();

    }

    /**
     * Registers the plugin as a sender.
     *
     * @param plugin The plugin to register.
     * @return The API helper class to send Slack messages.
     */
    public SlackSenderApi registerSender(final JavaPlugin plugin, final String webhook) {
        if (!registeredPlugins.contains(plugin)) {
            registeredPlugins.add(plugin);
        }
        return new SlackSenderApi(plugin, webhook);
    }

    protected ArrayList<JavaPlugin> getRegisteredPlugins() {
        return (ArrayList<JavaPlugin>) registeredPlugins.clone();
    }
}
