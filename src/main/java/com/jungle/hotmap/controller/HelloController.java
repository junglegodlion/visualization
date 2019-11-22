package com.jungle.hotmap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {
    @GetMapping(value = "/")
    public String sayHi() {
        return "Hello Spring Boot.";
    }
}