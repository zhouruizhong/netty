package com.zrz.netty.bio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author 周瑞忠
 * @description java类作用描述
 * @date 2019/5/2 14:53
 */
public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8888);
        socket.getOutputStream().write("Hello Server".getBytes());
        socket.getOutputStream().flush();

        System.out.println("消息发送成功，等待服务器返回");

        byte[] bytes = new byte[1024];
        int length = socket.getInputStream().read(bytes);
        System.out.println(new String(bytes, 0 , length));
        socket.close();
    }
}
