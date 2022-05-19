package com.example.order.springboot.repository;

import com.example.order.domain.model.Order;
import com.example.order.domain.repository.IOrderRepository;
import com.example.order.springboot.repository.jpa.OrderJPO;
import com.example.order.springboot.repository.jpa.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository implements IOrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public String save(Order order) {
        OrderJPO orderJPO = OrderJPO.createOrderJPO(order.getNumber(), order.getName(),
                order.getCount(), order.getCustomerName());

        orderJpaRepository.save(orderJPO);
        return orderJPO.getId();
    }
}
