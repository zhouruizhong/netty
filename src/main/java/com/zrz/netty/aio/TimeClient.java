package com.zrz.netty.aio;

public class TimeClient {

    private static String HOST = "127.0.0.1";

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new Thread(new AsyncTimeClientHandler(HOST, port), "AIO-AsyncTimeClientHandler-001").start();
    }
}
