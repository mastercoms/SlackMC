package org.circuitsoft.slack.api;

import org.json.simple.JSONObject;

import java.util.Objects;

public class SlackMessage {

    private String serverName;
    private String text;
    private final String username;
    private final boolean useMarkdown;

    /**
     * Posts a player sent message to Slack.
     *
     * @param message The message sent to Slack.
     * @param player The player that sent the message.
     * @param serverName The server the player is on.
     * @param useMarkdown Use markdown formatting in the message.
     */
    public SlackMessage(String message, String player, String serverName, boolean useMarkdown) {
        this(message,player, useMarkdown);
        this.serverName = serverName;
    }

    /**
     * Posts a player sent message to Slack.
     *
     * @param message The message sent to Slack.
     * @param player The player that sent the message.
     * @param useMarkdown Use markdown formatting in the message.
     */
    public SlackMessage(String message, String player, boolean useMarkdown) {
        this.text = message;
        this.username = player;
        this.useMarkdown = useMarkdown;
    }

    /**
     * Merges another message with this message
     *
     * @param slackMessage Message you want to merge with this message
     * @return if merge has succeeded.
     */
    public boolean append(SlackMessage slackMessage){
        if (username.equals(slackMessage.username) && Objects.equals(serverName, slackMessage.serverName)) {
            text += "\n" + slackMessage.text;
            return true;
        }
        return false;
    }

    /**
     * Get the payload message for slack
     *
     * @return PayloadString
     */
    public String getPayLoad(){
        JSONObject json = new JSONObject();
        json.put("text", text);
        if (serverName == null)
            json.put("username", username);
        else
            json.put("username", username + " (" + serverName + ")");
        json.put("icon_url", "https://cravatar.eu/helmhead/" + username + "/128.png");
        json.put("mrkdwn", useMarkdown);
        return json.toJSONString();
    }
}
