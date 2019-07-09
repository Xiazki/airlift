package com.airlift.springboot;

import com.airlift.spring.AirliftProperties;
import com.airlift.spring.ClientBeanPostProcessor;
import com.airlift.spring.ServerStarter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AirliftProperties.class})
public class AirliftAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClientBeanPostProcessor clientBeanPostProcessor() {
        return new ClientBeanPostProcessor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "airlift.server", name = "enable", havingValue = "true")
    public ServerStarter serverStarter() {
        return new ServerStarter();
    }

}
