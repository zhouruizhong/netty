package com.zrz.netty.io;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {

    private int sendNumber;
    private int counter;
    static final String ECHO_REQ = "Hi , ZhouRuizhong, Welcome to Netty.$_";

    public EchoClientHandler(){

    }

    public EchoClientHandler(int sendNumber){
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext context){
        for (int i = 0; i < 10; i++) {
            context.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception{
        System.out.println("This is " + ++counter +" time receive server : ["+ msg +"]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
