package com.zrz.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 周瑞忠
 * @description java类作用描述
 * @date 2019/5/2 14:53
 */
public class Server {

    public static void main(String[] args)  throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true){
            Socket socket = serverSocket.accept();
            new Thread(new TimeServerHandler(socket)).start();
        }
    }

}
