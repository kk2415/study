package com.example.order.springboot.rest;

import com.example.order.domain.model.OrderDto;
import com.example.order.springboot.messageq.KafkaProducer;
import com.example.order.springboot.service.IMsaServiceCoffeeMember;
import com.example.order.springboot.service.OrderServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiConroller {

    private final OrderServiceImpl orderService;
    private final KafkaProducer kafkaProducer;
    private final IMsaServiceCoffeeMember iMsaServiceCoffeeMember;

    @HystrixCommand
    @PostMapping("/coffee-order")
    public ResponseEntity<OrderDto> orderCoffee(@RequestBody OrderDto orderDto) throws JsonProcessingException {

        //is member
        if (iMsaServiceCoffeeMember.coffeeMember(orderDto.getName())) {
            System.out.println(orderDto.getName() + " is a member");
        }

        orderService.orderCoffee(orderDto);

        kafkaProducer.send("kafka-test", orderDto);
        return new ResponseEntity<OrderDto>(orderDto, HttpStatus.CREATED);
    }
}
