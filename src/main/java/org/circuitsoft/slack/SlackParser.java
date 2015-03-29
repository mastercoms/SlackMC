package org.circuitsoft.slack;

public class SlackParser {
    public SlackParser(String message) {
        if(message.contains("&l")) {
        }
        message.replaceAll("&", "&amp;");
        message.replaceAll("&", "&lt;");
        message.replaceAll(">", "&gt;");
    }
}
