package com.zrz.netty.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    private int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception{
        String body = (String) msg;
        System.out.println("Receive client: ["+msg+"]");

        //System.out.println("This is " + ++counter + " times receive client : [" + body + "]");
        body = "you are so stupid";
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        context.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable e){
        e.printStackTrace();
        context.close();
    }

}
