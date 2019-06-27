package com.zrz.netty.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class PerformTest {

    public static void main(String[] args) throws IOException {
         User user = new User();
         user.buildId(2).buidName("周瑞忠");

         int loop = 1000000;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            oos.flush();
            oos.close();
            byte[] b = baos.toByteArray();
            baos.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("The jdk serializable cost time is : " + (endTime - startTime) + "ms");
        System.out.println("--------------------------------------------------------------------");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            byte[] b = user.codeC(buffer);
        }
        endTime = System.currentTimeMillis();
        System.out.println("The byte array serializable cost time is : " + (endTime - startTime) + "ms");
    }
}
