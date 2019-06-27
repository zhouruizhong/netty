package com.zrz.netty.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<>(10);
        list.add("msgpack");
        list.add("kumofs");
        list.add("viver");

        MessagePack messagePack = new MessagePack();
        byte[] raw = messagePack.write(list);
        List<String> dst1 = messagePack.read(raw, Templates.tList(Templates.TString));
        System.out.println(dst1.get(0));
        System.out.println(dst1.get(1));
        System.out.println(dst1.get(2));
    }
}
