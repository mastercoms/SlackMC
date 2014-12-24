package org.circuitsoft.slack.bukkit;

import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

public class SlackBukkitGetter extends BukkitRunnable {

    public String token;
    public Server server;

    public SlackBukkitGetter(String token, Server server) {
        this.token = token;
        this.server = server;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void execute(String command) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
