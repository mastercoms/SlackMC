package org.circuitsoft.slackmc.plugin.bukkit.broadcaster;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogoutBroadcaster {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent event) {
    }
}
