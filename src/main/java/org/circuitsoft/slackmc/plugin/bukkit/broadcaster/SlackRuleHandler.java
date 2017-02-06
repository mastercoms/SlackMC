package org.circuitsoft.slackmc.plugin.bukkit.broadcaster;

import org.bukkit.event.Listener;
import org.circuitsoft.slackmc.api.message.SlackMessage;
import org.circuitsoft.slackmc.api.message.SlackMessageTemplate;
import org.circuitsoft.slackmc.api.outgoing.SlackSender;
import org.circuitsoft.slackmc.plugin.bukkit.filters.SlackFilter;

public class SlackRuleHandler implements Listener {
    protected final String webhook;
    protected SlackMessageTemplate template;
    private SlackSender sender;
    private final SlackFilter[] filters;

    public SlackRuleHandler(String webhook, SlackMessageTemplate template, SlackSender sender, SlackFilter[] filters) {
        this.webhook = webhook;
        this.template = template;
        this.sender = sender;
        this.filters = filters;
    }

    public void send(SlackMessage message) {
        sender.send(webhook, message);
        System.out.println("sent.");
    }
}
