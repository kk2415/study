package com.example.order.springboot.repository.jpa;

import com.example.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderJPO, String> {
}
