package com.zrz.netty.websocket;

import com.sun.corba.se.spi.activation.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetAddress;

public class WebSocketServer {

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            // HttpServerCodec：将请求和应答消息解码为HTTP消息
                            channelPipeline.addLast("http-codec", new HttpServerCodec());
                            // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
                            channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            // ChunkedWriteHandler：向客户端发送HTML5文件
                            channelPipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            // 在管道中添加我们自己的接收数据实现方法
                            channelPipeline.addLast("handler", new WebSocketServerHandler());
                        }
                    });

            Channel channel = bootstrap.bind(port).sync().channel();
            System.out.println("Web socket server started at port : " + port +".");
            System.out.println("Open your browser and navigate to http://192.168.1.104:" + port + "/");
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new WebSocketServer().run(port);
    }
}
