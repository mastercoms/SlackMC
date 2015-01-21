package org.circuitsoft.slack.bukkit;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.util.log.Logger;

public class SlackBukkitGetter {
    private JavaPlugin plugin;
    private ConfigurationSection configuration;
    private org.bukkit.Server bukkitServer;
    private String token;
    private Server webServer;

    public SlackBukkitGetter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig().getConfigurationSection("incoming-webhook");
        this.bukkitServer = plugin.getServer();
        this.token = configuration.getString("token");
    }

    public void run() {
        org.eclipse.jetty.util.log.Log.setLog(new JettyNullLogger());
        String webhostname = configuration.getString("bindaddress", bukkitServer.getIp());
        if (webhostname.equals("")) webhostname = "0.0.0.0";
        int webport = configuration.getInt("port", 25560);
        int maxconnections = configuration.getInt("max-connections", 30);
        if (maxconnections < 2) maxconnections = 2;

        webServer = new Server();

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(maxconnections);
        ExecutorThreadPool pool = new ExecutorThreadPool(2, maxconnections, 60, TimeUnit.SECONDS, queue);
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setMaxIdleTime(5000);
        connector.setAcceptors(1);
        connector.setAcceptQueueSize(50);
        connector.setLowResourcesMaxIdleTime(1000);
        connector.setLowResourcesConnections(maxconnections / 2);
        if (!webhostname.equals("0.0.0.0"))
            connector.setHost(webhostname);
        connector.setPort(webport);
        HandlerList hlist = new HandlerList();
        hlist.setHandlers(new org.eclipse.jetty.server.Handler[] { new WebhookHandler() });

        webServer.setThreadPool(pool);
        webServer.setConnectors(new Connector[] { connector });
        webServer.setStopAtShutdown(true);
        webServer.setHandler(hlist);

        try {
            webServer.start();
            plugin.getLogger().info("Web server started on address " + webhostname + ":" + webport);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to start WebServer on address " + webhostname + ":" + webport + " : " + e.getMessage());
        }
    }

    public void stop() {
        if (webServer != null) {
            try {
                webServer.stop();
                for(int i = 0; i < 100; i++) {    /* Limit wait to 10 seconds */
                    if (webServer.isStopping())
                        Thread.sleep(100);
                }
                if(webServer.isStopping()) {
                    plugin.getLogger().warning("Graceful shutdown timed out - continuing to terminate");
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to stop WebServer!");
            }
            webServer = null;
        }
    }

    public class WebhookHandler extends AbstractHandler {
        public WebhookHandler() {
        }

        @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                if (!request.getRequestURI().isEmpty() && !request.getRequestURI().equals("/")) {
                    response.sendError(404);
                } else if (!request.getMethod().equalsIgnoreCase("POST")) {
                    response.sendError(405);
                } else {
                    String token = request.getParameter("token");
                    String username = request.getParameter("user_name");
                    String message = request.getParameter("text");
                    if (token == null || !token.equals(SlackBukkitGetter.this.token)) {
                        response.sendError(403);
                    } else {
                        if (!username.equals("slackbot")) {
                            String msg;
                            String msgfmt = configuration.getString("msgformat", null);
                            if (msgfmt != null) {
                                msgfmt = unescapeString(msgfmt);
                                msg = msgfmt.replace("%username%", username).replace("%message%", message);
                            } else {
                                msg = unescapeString(SlackBukkitGetter.this.configuration.getString("msgprefix", "\u00A72[Slack] ")) + username + ": " + unescapeString(SlackBukkitGetter.this.configuration.getString("msgsuffix", "\u00A7f")) + message;
                            }
                            bukkitServer.broadcastMessage(msg);
                        }
                        response.setStatus(200);
                        baseRequest.setHandled(true);
												response.getWriter().print("{ \"text\": \"\" }");
                    }
                }
            }

        public String unescapeString(String v) {
            return v.replace("&color;", "\u00A7");
        }
    }

    public class JettyNullLogger implements Logger {
        public String getName() {
            return "Slack";
        }
        public void warn(String s, Object... objects) {}
        public void warn(Throwable throwable) {}
        public void warn(String s, Throwable throwable) {}
        public void info(String s, Object... objects) {}
        public void info(Throwable throwable) {}
        public void info(String s, Throwable throwable) {}
        public boolean isDebugEnabled() {
            return false;
        }
        public void setDebugEnabled(boolean b) {}
        public void debug(String s, Object... objects) {}
        public void debug(Throwable throwable) {}
        public void debug(String s, Throwable throwable) {}
        public Logger getLogger(String s) {
            return this;
        }
        public void ignore(Throwable throwable) {}
    }
}
