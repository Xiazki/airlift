package com.airlift.spring.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Service
public @interface AirliftService {
}
