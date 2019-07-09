package com.airlift.spring;

import com.airlift.client.AirliftClientFactory;
import com.airlift.client.config.ClientConfig;
import com.airlift.spring.annotation.AirliftClient;
import com.airlift.spring.exception.HostNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {


    @Autowired
    private AirliftProperties airliftProperties;
    private ApplicationContext applicationContext;
    private ConcurrentHashMap<String, AirliftClientFactory<?>> factoryMap = new ConcurrentHashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                AirliftClient airliftClient = field.getDeclaredAnnotation(AirliftClient.class);
                if (airliftClient == null) {
                    continue;
                }
                field.set(bean, getClient(field.getType(), airliftClient));
            } catch (IllegalAccessException e) {
                throw new BeanCreationException("inject airlift client failed,client name:" + field.getName());
            }
        }
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Object getClient(Class<?> clazz, AirliftClient airliftClient) {
        AirliftClientFactory<?> clientFactory = factoryMap.get(clazz.getName());
        if (clientFactory == null) {
            factoryMap.putIfAbsent(clazz.getName(), new AirliftClientFactory<>(clazz, buildClientConfig(airliftClient)));
            clientFactory = factoryMap.get(clazz.getName());
        }
        return clientFactory.get();
    }

    //todo 完善属性相关配置
    private ClientConfig buildClientConfig(AirliftClient airliftClient) {
        ClientConfig.ClientConfigBuilder builder = ClientConfig.builer();
        if (!StringUtils.isEmpty(airliftClient.host())) {
            return builder.withHost(airliftClient.host()).withPort(airliftClient.port()).build();
        } else if (StringUtils.isEmpty(airliftProperties.getRegistryUrls())) {
            throw new HostNotFoundException("this client doesn't hast a host or registry url");
        }
        return builder.withNeedRegistry(true).withRegistryUrls(airliftProperties.getRegistryUrls()).build();
    }

}
