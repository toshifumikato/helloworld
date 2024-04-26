package com.example.helloworld.service;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldServiceImpl.class);
    @Override
    @Observed(name = "user.name", contextualName = "x-span-id")
    public String hello() {
        return "Hello World!!!";
    }
}
