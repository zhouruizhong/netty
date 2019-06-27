package com.zrz.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaderUtil.isKeepAlive;

/**
 * @author zrz
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker handlshaker;

    /**
     * channel 通道 action 活跃的 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端连接开启：" + ctx.channel().remoteAddress().toString());
    }

    /**
     * channel 通道 Inactive 不活跃的 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端连接关闭：" + ctx.channel().remoteAddress().toString());
    }

    /**
     * 接收客户端发送的消息 channel 通道 Read 读 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void messageReceived(ChannelHandlerContext context, Object o) throws Exception {
        // 传统的http接入
        if (o instanceof FullHttpRequest) {
            handleHttpRequest(context, (FullHttpRequest) o);
        } else if (o instanceof WebSocketFrame) {
            System.out.println(handlshaker.uri());
            handleWebSocketFrame(context, (WebSocketFrame) o);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception {
        context.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // 如果http解码失败，返回http异常
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://192.168.1.104:8080/websocket", null, false);
        handlshaker = wsFactory.newHandshaker(req);
        if (handlshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handlshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext context, WebSocketFrame frame) {
        // 判断是否是关闭链路的指令，
        if (frame instanceof CloseWebSocketFrame) {
            handlshaker.close(context.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        //判断是否是ping 消息
        if (frame instanceof PingWebSocketFrame) {
            context.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 本例程仅支持文本消息，不支持二级制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            System.out.println("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format("%s frame types not supported ", frame.getClass().getName()));
        }

        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        System.out.println("服务端收到：" + request);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("%s received %s", context.channel(), request));
        }

        context.channel().write(new TextWebSocketFrame(request + ", 欢迎使用 Netty WebSocket 服务，现在时刻：" + new Date().toString()));
    }

    private static void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest request, DefaultFullHttpResponse response) {
        //返回应答给客户端
        if (response.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            //setContentLength(response, response.content().readableBytes());
        }

        //如果是非keep-alive, 关闭连接
        ChannelFuture future = context.channel().writeAndFlush(response);
        if (!isKeepAlive(request) || response.status().code() != 200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
