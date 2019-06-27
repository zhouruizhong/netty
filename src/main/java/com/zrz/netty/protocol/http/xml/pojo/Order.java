package com.zrz.netty.protocol.http.xml.pojo;

import lombok.Data;

@Data
public class Order {
    private Long orderNumber;
    private Customer customer;
    private Address billTo;
    private Shipping shipping;
    private Address shipTo;
    private Float total;
}
