package org.circuitsoft.slack.api;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SlackPoster implements Runnable {

    private String webhookUrl;
    private volatile List<SlackMessage> messages = new ArrayList<>();

    public SlackPoster(String webhookUrl){
        this.webhookUrl = webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void addMessage(SlackMessage slackMessage){
        if (messages.size() > 0) {
            SlackMessage oldmessage = messages.get(messages.size() - 1);
            if (oldmessage.append(slackMessage))
                return;
        }
        messages.add(slackMessage);
    }

    @Override
    public void run() {
        while (true){
            try {
                if (messages.size() > 0) {
                    post(messages.get(0));
                    messages.remove(0);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void post(SlackMessage message){
        String jsonStr = "payload=" + message.getPayLoad();
        System.out.println("post: "+ message.getPayLoad());
        try {
            HttpURLConnection webhookConnection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            webhookConnection.setRequestMethod("POST");
            webhookConnection.setDoOutput(true);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream())) {
                bufOut.write(jsonStr.getBytes(StandardCharsets.UTF_8));
                bufOut.flush();
                bufOut.close();
            }
            int serverResponseCode = webhookConnection.getResponseCode();
            System.out.println(webhookConnection.getResponseMessage());
            System.out.println(serverResponseCode);
            webhookConnection.disconnect();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
