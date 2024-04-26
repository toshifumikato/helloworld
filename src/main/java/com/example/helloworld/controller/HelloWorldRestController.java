package com.example.helloworld.controller;

import com.example.helloworld.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class HelloWorldRestController {
    Logger logger = LoggerFactory.getLogger(HelloWorldRestController.class);
    private HelloWorldService service;

    public HelloWorldRestController(HelloWorldService service) {
        this.service = service;
    }

    @GetMapping(path = "/hello")
    public String hello() {
        logger.debug("hello method called.");
        return service.hello();
    }
}
