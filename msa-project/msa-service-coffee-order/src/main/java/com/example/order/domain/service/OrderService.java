package com.example.order.domain.service;

import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderDto;
import com.example.order.domain.repository.IOrderRepository;

public class OrderService implements IOrderService {

    private IOrderRepository orderRepository;

    public OrderService(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String orderCoffee(OrderDto orderDto) {
        Order order = Order.builder()
                .id(orderDto.getId())
                .number(orderDto.getNumber())
                .name(orderDto.getName())
                .count(orderDto.getCount())
                .customerName(orderDto.getCustomerName())
                .build();

        orderRepository.save(order);
        return order.getId();
    }

}
