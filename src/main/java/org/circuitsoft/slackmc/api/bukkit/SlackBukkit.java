package org.circuitsoft.slackmc.api.bukkit;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SlackBukkit extends JavaPlugin implements Listener {

    private static final SlackPluginApi slackPluginApi = new SlackPluginApi();

    public static SlackPluginApi getSlackPluginApi() {
        return slackPluginApi;
    }

    @Override
    public void onEnable() {
        getServer().getScheduler().runTaskAsynchronously(this, new SlackReceiveServer(this));
    }
}
