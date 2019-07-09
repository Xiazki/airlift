package com.airlift.spring.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface AirliftClient {

    String host();

    int port();

}
