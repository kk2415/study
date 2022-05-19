package com.example.order.domain.model;

import lombok.Data;

@Data
public class OrderDto {

    private String id;
    private String number;
    private String name;
    private String count;
    private String customerName;

}
