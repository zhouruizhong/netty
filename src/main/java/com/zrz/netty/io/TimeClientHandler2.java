package com.zrz.netty.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

public class TimeClientHandler2 extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler2.class.getName());
    private int counter;
    private byte[] req;

    public TimeClientHandler2(){
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
        String body = (String) msg;
        System.out.println("Now is : " + body + "; the counter is : " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable e){
        // 释放资源
        logger.warning("Unexpected exception from downstream " + e.getMessage());
        context.close();
    }
}
