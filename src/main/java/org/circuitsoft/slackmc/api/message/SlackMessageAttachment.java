package org.circuitsoft.slackmc.api.message;

import java.util.ArrayList;

public class SlackMessageAttachment {
    private String fallback;
    private String color;
    private String pretext;
    private SlackMessageAttachmentAuthor author;
    private SlackMessageAttachmentTitle title;
    private String text;
    private ArrayList<SlackMessageAttachmentField> fields = new ArrayList<>();
    private String imageUrl;
    private String thumbUrl;
    private SlackMessageAttachmentFooter footer;

    public SlackMessageAttachment(String fallback) {
        this.fallback = fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }
}
