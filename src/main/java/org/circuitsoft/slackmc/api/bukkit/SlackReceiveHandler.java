package org.circuitsoft.slackmc.api.bukkit;

import net.gpedro.integrations.slack.SlackMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.circuitsoft.slackmc.api.bukkit.SlackBukkit.getSlackPluginApi;

public class SlackReceiveHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getMethod().equals("POST")) {
            SlackMessage message = new SlackMessage(request.getParameter("channel_name"), request.getParameter("user_name"), request.getParameter("text").split(":", 2)[1]);
            String pluginName = request.getParameter("text").split(":")[0];
            for (JavaPlugin plugin : getSlackPluginApi().getRegisteredPlugins()) {
                if (plugin instanceof SlackListener && pluginName.equals(plugin.getDescription().getName())) {
                    ((SlackListener) plugin).onSlackMessage(request.getParameter("token"), message);
                    break;
                }
            }
        }
    }
}
