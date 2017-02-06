package org.circuitsoft.slackmc.api.message;

public class SlackMessageAttachmentField {
    private String title;
    private String value;
    private boolean sideBySide;

    public SlackMessageAttachmentField(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSideBySide() {
        return sideBySide;
    }

    public void setSideBySide(boolean sideBySide) {
        this.sideBySide = sideBySide;
    }
}
