package com.zrz.netty.aio;

public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new Thread(new AsyncTimeServerHandler(port), "AIO-AsyncTimeServerHandler-001").start();
    }
}
