package com.airlift.springtest;

import com.airlift.springboot.annotation.EnableAirlift;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAirlift
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
