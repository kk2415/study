package com.example.status.springboot.rest;

import com.example.status.springboot.repository.Status;
import com.example.status.springboot.repository.StatusDto;
import com.example.status.springboot.repository.StatusJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StatusApiController {

    @Autowired private StatusJpaRepository statusJpaRepository;

    @PostMapping("/coffee-order-status")
    public ResponseEntity<StatusDto> orderStatus() {
        List<Status> findStatus = statusJpaRepository.findAll();

        List<StatusDto> collect = findStatus.stream().map(s -> new StatusDto(s.getId(), s.getOrderHistory()))
                .collect(Collectors.toList());

        return new ResponseEntity(collect, HttpStatus.OK);
    }

}
