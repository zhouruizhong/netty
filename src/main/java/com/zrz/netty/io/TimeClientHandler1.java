package com.zrz.netty.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

public class TimeClientHandler1 extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler1.class.getName());
    private int counter;
    private byte[] req;

    public TimeClientHandler1(){
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext context){
        ByteBuf byteBuf = null;
        for (int i = 0; i < 100; i++) {
            byteBuf = Unpooled.buffer(req.length);
            byteBuf.writeBytes(req);
            context.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is : " + body + "; the counter is : " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable e){
        // 释放资源
        logger.warning("Unexpected exception from downstream " + e.getMessage());
        context.close();
    }
}
