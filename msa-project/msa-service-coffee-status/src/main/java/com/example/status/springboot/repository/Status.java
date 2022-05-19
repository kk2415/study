package com.example.status.springboot.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Status {

    @Id
    @GeneratedValue
    private Long id;

    private String orderHistory;

    public Status(String orderHistory) {
        this.orderHistory = orderHistory;
    }
}
