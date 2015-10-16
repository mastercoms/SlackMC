package org.circuitsoft.slackmc.api.bukkit;

import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Server;

public class SlackReceiveServer extends BukkitRunnable {

    @Override
    public void run() {
        final Server server = new Server(8080);
        server.setHandler(new SlackReceiveHandler());
        try {
            server.start();
            server.join();
        } catch (Exception ignored) {
        }
    }
}
