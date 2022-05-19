package com.example.order.springboot.service;

import com.example.order.domain.repository.IOrderRepository;
import com.example.order.domain.service.OrderService;

public class OrderServiceImpl extends OrderService {

    public OrderServiceImpl(IOrderRepository orderRepository) {
        super(orderRepository);
    }
}
