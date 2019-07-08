package com.airlift.spring;

import com.airlift.client.AirliftClientFactory;
import com.airlift.spring.annotation.AirliftClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private ConcurrentHashMap<String, AirliftClientFactory<?>> factoryMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getFields();
        for (Field field : fields) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                AirliftClient airliftClient = field.getDeclaredAnnotation(AirliftClient.class);
                if (airliftClient == null) {
                    continue;
                }
                field.set(bean, getClient(field.getType()));
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

    //todo
    private Object getClient(Class<?> clazz) {


        return null;
    }

}
