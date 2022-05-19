package com.example.order.springboot.repository.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class OrderJPO {

    @Id @GeneratedValue
    private String id;

    private String number;
    private String name;
    private String count;
    private String customerName;

    public static OrderJPO createOrderJPO(String number, String name, String count, String customerName) {
        OrderJPO order = new OrderJPO();
        order.setNumber(number);
        order.setName(name);
        order.setCount(count);
        order.setCustomerName(customerName);

        return order;
    }

}
