package org.circuitsoft.slackmc.api.message;

public class SlackMessageAttachmentTitle {
    private String title;
    private String titleLink;

    public SlackMessageAttachmentTitle(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleLink() {
        return titleLink;
    }
}
