package com.zrz.netty.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Test {

    public static void main(String[] args) throws IOException {
        User user = new User();
        user.buildId(1).buidName("周瑞忠");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(user);
        os.flush();
        os.close();
        byte[] bytes = bos.toByteArray();
        System.out.println("The jdk serializable length is : " + bytes.length);
        bos.close();
        System.out.println("------------------------------------------------");
        System.out.println("The byte array serializable lenght is : " + user.codeC().length);
    }
}
