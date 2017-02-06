package org.circuitsoft.slackmc.api.message;

public class SlackMessageTemplate {
    private String messageFormat = "{0}";
    private String channel = "";
    private boolean unfurlLinks = true;

    public SlackMessageTemplate() {
    }

    public SlackMessageTemplate(String channel, boolean unfurlLinks) {
        this.channel = channel;
        this.unfurlLinks = unfurlLinks;
    }

    public SlackMessageTemplate(String channel, boolean unfurlLinks, String messageFormat) {
        this.channel = channel;
        this.unfurlLinks = unfurlLinks;
        this.messageFormat = messageFormat;
    }

    public SlackMessage toMessage(String text) {
        if (!channel.isEmpty()) {
            return new SlackMessage(text).setUnfurlLinks(unfurlLinks).setChannel(channel);
        }
        return new SlackMessage(text).setUnfurlLinks(unfurlLinks);
    }

    public SlackMessage toMessage(Object... args) {
        return toMessage(String.format(messageFormat, args));
    }
}
