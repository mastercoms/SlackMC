package org.circuitsoft.slackmc.plugin.bukkit.broadcaster;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.circuitsoft.slackmc.api.message.SlackMessageTemplate;
import org.circuitsoft.slackmc.api.outgoing.SlackSender;
import org.circuitsoft.slackmc.plugin.bukkit.filters.SlackFilter;

public class ChatRuleHandler extends SlackRuleHandler {
    public ChatRuleHandler(String webhook, SlackMessageTemplate template, SlackSender sender, SlackFilter[] filters) {
        super(webhook, template, sender, filters);
        System.out.println("chat handler started");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        System.out.println("chat handler called");
        send(template.toMessage(event.getPlayer().getName())
                .setUsername(event.getPlayer().getName()));
    }
}
