package org.circuitsoft.slackmc.api.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlackMessage {
    private String text;
    private String username;
    private String icon;
    private String channel;
    private ArrayList<SlackMessageAttachment> attachments = new ArrayList<>();
    private boolean unfurlLinks;
    private int tries = 5;

    public SlackMessage(String text) {
        this.text = text;
    }

    public SlackMessage(String text, int tries) {
        this.text = text;
        this.tries = tries;
    }

    public SlackMessage setText(String text) {
        this.text = text;
        return this;
    }

    public SlackMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    public SlackMessage setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public SlackMessage setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public SlackMessage addAttachments(SlackMessageAttachment... attachments) {
        if (this.attachments.size() + attachments.length <= 100) {
            this.attachments.addAll(Arrays.asList(attachments));
        } else {
            int length = this.attachments.size() + attachments.length - 100;
            if (length > 0) {
                this.attachments.addAll(Arrays.asList(Arrays.copyOfRange(attachments, 0, length)));
            }
        }
        return this;
    }

    public void tried() {
        tries--;
    }

    public boolean hi() {
        return tries <= 0;
    }

    public List<SlackMessageAttachment> getAttachments() {
        return attachments;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public String getIcon() {
        return icon;
    }

    public String getChannel() {
        return channel;
    }

    public boolean isUnfurlLinks() {
        return unfurlLinks;
    }

    public SlackMessage setUnfurlLinks(boolean unfurlLinks) {
        this.unfurlLinks = unfurlLinks;
        return this;
    }

    public String toJsonString() {
        return "payload={\"text\": \"This is a line of text in a channel.\\nAnd this is another line of text.\"}";
    }

    public SlackMessage fromJson(String json) {
        return new SlackMessage("");
    }
}
