package com.zrz.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 周瑞忠
 * @date 2019/5/2 14:53
 */
public class Server {

    public static void main(String[] args)  throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true){
            // 阻塞方法
            Socket socket = serverSocket.accept();
            new Thread(() ->{
                    new TimeServerHandler(socket);
            }).start();
        }
    }

}
