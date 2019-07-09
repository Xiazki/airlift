package com.airlift.springtest.controller;

import com.airlift.spring.annotation.AirliftClient;
import com.airlift.springtest.api.HelloWorldApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @AirliftClient
    private HelloWorldApi helloWorldApi;

    @GetMapping
    public String get(@RequestParam("name") String name){
        return helloWorldApi.getHi(name).getMessage();
    }

}
