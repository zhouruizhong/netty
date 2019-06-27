package com.zrz.netty.serialization;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author zrz
 *
 */

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;

    public User buildId(Integer id){
        this.id = id;
        return this;
    }

    public User buidName(String name){
        this.name = name;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] codeC(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] value = this.name.getBytes();
        byteBuffer.putInt(value.length);
        byteBuffer.put(value);
        byteBuffer.putInt(this.id);
        byteBuffer.flip();

        value = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }

    public byte[] codeC(ByteBuffer byteBuffer){
        byteBuffer.clear();
        byte[] value = this.name.getBytes();
        byteBuffer.putInt(value.length);
        byteBuffer.put(value);
        byteBuffer.putInt(this.id);
        byteBuffer.flip();

        value = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }
}
