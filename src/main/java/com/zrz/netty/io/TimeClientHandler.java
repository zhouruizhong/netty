package com.zrz.netty.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
    private final ByteBuf byteBuf;

    public TimeClientHandler(){
        byte[] req = "QUERY TIME ORDER".getBytes();
        byteBuf = Unpooled.buffer(req.length);
        byteBuf.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext context){
        context.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is : " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable e){
        // 释放资源
        logger.warning("Unexpected exception from downstream" + e.getMessage());
        context.close();
    }
}
