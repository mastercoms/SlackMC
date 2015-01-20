package org.circuitsoft.slack.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import org.circuitsoft.slack.api.BungeePoster;

public class SlackBungeeCommand extends Command {

    public static final BaseComponent[] helpMsg = new ComponentBuilder("/slack send <username> <image URL or null for username's skin> <message> - send a custom message to slack\n/slack reload - reload Slack's config").color(ChatColor.GOLD).create();
    private static final BaseComponent[] sendHelpMsg = new ComponentBuilder("/slack send <username> <image URL or null for username's skin> <message>").color(ChatColor.GOLD).create();
    private static final BaseComponent[] reloadHelpMsg = new ComponentBuilder("/slack reload - reload Slack's config").color(ChatColor.GOLD).create();
    private static final BaseComponent[] reloadMsg = new ComponentBuilder("Slack has been reloaded.").color(ChatColor.GREEN).create();
    private static final BaseComponent[] noPermMsg = new ComponentBuilder("You are not allowed to execute this command!").color(ChatColor.DARK_RED).create();

    private final SlackBungee plugin;

    public SlackBungeeCommand(SlackBungee plugin) {
        super("slack");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("slack.command")) {
            sender.sendMessage(noPermMsg);
        } else if (args.length == 0) {
            sender.sendMessage(helpMsg);
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("slack.command")) {
                plugin.reloadConfig();
                sender.sendMessage(reloadMsg);
                if (!sender.equals(ProxyServer.getInstance().getConsole())) {
                    ProxyServer.getInstance().getConsole().sendMessage(new ComponentBuilder("Slack has been reloaded by " + sender.getName() + '.').color(ChatColor.GREEN).create());
                }
            } else {
                sender.sendMessage(noPermMsg);
            }
        } else if (args[0].equals("send")) {
            if (sender.hasPermission("slack.command")) {
                if (args.length <= 3) {
                    sender.sendMessage(sendHelpMsg);
                } else if (args.length >= 4) {
                    StringBuilder sb = new StringBuilder();
                    boolean first = true;
                    for (int i = 3; i < args.length; i++) {
                        if (first) {
                            first = false;
                        } else {
                            sb.append(" ");
                        }
                        sb.append(args[i]);
                    }
                    plugin.getProxy().getScheduler().runAsync(plugin, new BungeePoster(sb.toString(), args[1], args[2].equalsIgnoreCase("null") ? null : args[2]));
                }
            } else {
                sender.sendMessage(noPermMsg);
            }
        } else {
            sender.sendMessage(helpMsg);
        }
    }
}
