package org.circuitsoft.slackmc.api.message;

public class SlackMessageAttachmentAuthor {
    private String name;
    private String link;
    private String icon;

    public SlackMessageAttachmentAuthor(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getIcon() {
        return icon;
    }
}
