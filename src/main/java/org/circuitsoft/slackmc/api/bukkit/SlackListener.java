package org.circuitsoft.slackmc.api.bukkit;

import net.gpedro.integrations.slack.SlackMessage;

/**
 * Implement this class to enable events from Slack.
 */
@FunctionalInterface
public interface SlackListener {

    /**
     * This method is called by the API when an event from Slack occurs that involves the plugin.
     */
    void onSlackMessage(String token, SlackMessage message);
}
