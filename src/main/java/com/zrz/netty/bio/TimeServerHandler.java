package com.zrz.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author 周瑞忠
 * @description java类作用描述
 * @date 2019/5/2 14:59
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(){

    }

    public TimeServerHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while (true){
                body = in.readLine();
                if (body == null){
                    break;
                }
                System.out.println("The time server receive order:" + body);
                currentTime = "".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() :"BAD ORDER";
                out.println(currentTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (in != null){
                try{
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (out != null){
                out.close();
                out = null;
            }
            if (this.socket != null){
                try{
                    this.socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                this.socket = null;

            }
        }

    }
}
