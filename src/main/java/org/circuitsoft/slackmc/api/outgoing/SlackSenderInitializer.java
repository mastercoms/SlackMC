package org.circuitsoft.slackmc.api.outgoing;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;
import org.circuitsoft.slackmc.api.message.SlackMessage;

public class SlackSenderInitializer extends ChannelInitializer<SocketChannel> {

    private SslContext sslCtx;
    private final SlackSender sender;
    private final String webhookUrl;
    private SlackMessage message;

    public SlackSenderInitializer(SslContext sslCtx, SlackSender sender, String webhookUrl, SlackMessage message) {
        this.sslCtx = sslCtx;
        this.sender = sender;
        this.webhookUrl = webhookUrl;
        this.message = message;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("ssl", sslCtx.newHandler(ch.alloc()));
        pipeline.addLast("codec", new HttpClientCodec());
        pipeline.addLast("inflater", new HttpContentDecompressor());
        pipeline.addLast("handler", new SlackSenderHandler(sender, webhookUrl, message));
    }
}
