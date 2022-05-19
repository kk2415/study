package com.example.order.domain.repository;

import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderDto;
import com.example.order.springboot.repository.jpa.OrderJPO;

public interface IOrderRepository {

    public String save(Order order);

}
