package com.example.order.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

    private String id;
    private String number;
    private String name;
    private String count;
    private String customerName;

}
