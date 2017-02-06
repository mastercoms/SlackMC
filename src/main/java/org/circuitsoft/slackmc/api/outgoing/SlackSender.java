package org.circuitsoft.slackmc.api.outgoing;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.circuitsoft.slackmc.api.message.SlackMessage;

import javax.net.ssl.SSLException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackSender {

    private SslContext sslCtx;
    private Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup group = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

    public SlackSender() {
        try {
            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
            bootstrap.group(group)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true);
        } catch (SSLException e) {
            e.printStackTrace();
        }

    }

    public void send(String webhookUrl, SlackMessage message) {
        //if (message.getTries() <= 5) {
        try {
            System.out.println(webhookUrl);
            URL url = new URL(webhookUrl);
            System.out.println("host:" + url.getHost() + "\nPort:" + (url.getPort() == -1 ? url.getDefaultPort() : url.getPort()));
        bootstrap.handler(new SlackSenderInitializer(sslCtx, this, webhookUrl, message));
            ByteBuf buf = Unpooled.copiedBuffer(message.toJsonString(), StandardCharsets.UTF_8);
            try {
                Channel channel = bootstrap.connect(url.getHost(), url.getPort() == -1 ? url.getDefaultPort() : url.getPort()).channel();
                FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, webhookUrl);
                request.headers()
                        .set(HttpHeaders.Names.HOST, url.getHost())
                        .set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE)
                        .add(HttpHeaders.Names.CONTENT_TYPE, "application/json")
                        .set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
                request.content().clear().writeBytes(buf);
                channel.writeAndFlush(request);
                //message.tried();
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        bootstrap.group().shutdownGracefully();
    }
}
