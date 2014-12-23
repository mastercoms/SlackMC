package us.circuitsoft.slack.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import us.circuitsoft.slack.api.BungeePoster;

public class SlackBungeeCommand extends Command {

    private final SlackBungee plugin;

    public SlackBungeeCommand(SlackBungee plugin) {
        super("slack");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new ComponentBuilder("/slack send <username> <image URL> <message> - send a custom message to slack \n/slack reload - reload Slack's config").color(ChatColor.GOLD).create());
        }
        if (strings[0].equals("send") && strings.length <= 3) {
            commandSender.sendMessage(new ComponentBuilder("/slack send <username> <image URL> <message>").color(ChatColor.GOLD).create());
        } else if (strings[0].equals("send") && strings.length >= 4) {
            String m = "";
            for (int i = 3; i < strings.length; i++) {
                m = m + strings[i] + " ";
            }
            plugin.getProxy().getScheduler().runAsync(plugin, new BungeePoster(m, strings[1], strings[2]));
        }
    }
}
