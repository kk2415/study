package com.example.member.springboot.rest;

import com.example.member.springboot.repository.MemberJpaRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberApiController {
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @HystrixCommand
    @GetMapping("/coffee-member/v1.0/{name}")
    public boolean coffeeMember(@PathVariable String name) {
        if (memberJpaRepository.existsByName(name)) {
            return false;
        }
        return true;
    }


    @HystrixCommand(fallbackMethod = "fallbackFunction")
    @GetMapping("/fallback-test")
    public String fallBackTest() throws Throwable {
        throw new Throwable("fallbackTest");
    }

    public String fallbackFunction() {
        return "fallbackFunction()";
    }
}
