package com.zrz.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (null != args && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket(port);
            System.out.println("The time server is start in port:" + port);

            Socket socket = null;
            while (true){
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (null != serverSocket){
                try {
                    System.out.println("The time server is close");
                    serverSocket.close();
                    serverSocket = null;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }





}
