package com.example.status.springboot.repository;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class StatusDto {

    private Long id;
    private String orderHistory;

    public StatusDto(Long id, String orderHistory) {
        this.id = id;
        this.orderHistory = orderHistory;
    }
}
