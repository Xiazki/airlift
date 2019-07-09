package com.airlift.springboot.annotation;

import com.airlift.springboot.AirliftAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AirliftAutoConfiguration.class)
public @interface EnableAirlift {
}
