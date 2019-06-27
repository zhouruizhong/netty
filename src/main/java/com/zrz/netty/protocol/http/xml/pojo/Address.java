package com.zrz.netty.protocol.http.xml.pojo;

import lombok.Data;

@Data
public class Address {

    private String street1;
    private String street2;
    private String city;
    private String state;
    private String postCode;
    private String country;
}
