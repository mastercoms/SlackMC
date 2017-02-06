package org.circuitsoft.slackmc.api.message;

public class SlackMessageAttachmentFooter {
    private String text;
    private String icon;
    private String time;

    public SlackMessageAttachmentFooter(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
