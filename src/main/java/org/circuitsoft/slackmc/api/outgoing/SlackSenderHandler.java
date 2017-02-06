package org.circuitsoft.slackmc.api.outgoing;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import org.circuitsoft.slackmc.api.message.SlackMessage;

public class SlackSenderHandler extends SimpleChannelInboundHandler<HttpObject> {

    private final SlackSender sender;
    private final String webhookUrl;
    private SlackMessage message;

    public SlackSenderHandler(SlackSender sender, String webhookUrl, SlackMessage message) {
        this.sender = sender;
        this.webhookUrl = webhookUrl;
        this.message = message;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            if (response.getStatus().code() == 500 /* && message.getTries() <= 5*/) {
                sender.send(webhookUrl, message);
            }
        }
    }
}
