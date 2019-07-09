package com.airlift.spring.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface AirliftService {
}
