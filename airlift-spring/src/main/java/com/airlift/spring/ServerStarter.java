package com.airlift.spring;

import com.airlift.server.AirliftServer;
import com.airlift.server.config.ServerConfig;
import com.airlift.spring.annotation.AirliftService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;


public class ServerStarter implements CommandLineRunner, DisposableBean, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ServerStarter.class);

    private ApplicationContext applicationContext;
    private AirliftServer airliftServer;
    @Autowired
    private AirliftProperties airliftProperties;


    @Override
    public void run(String... args) throws Exception {
        if (!airliftProperties.getServer().isEnable()) {
            return;
        }
        logger.info("init airlift server to spring");
        List<Object> services = scanApplicationContent();
        if (services.isEmpty()) {
            logger.info("no service found");
            return;
        }
        airliftServer = new AirliftServer(buildServerConfig(), services);
        try {
            airliftServer.start();
        } catch (Exception e) {
            logger.error("start failed");
            throw e;
        }
    }

    @Override
    public void destroy() throws Exception {
        if (airliftServer != null) {
            airliftServer.close();
            logger.info("close airlift server");
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private List<Object> scanApplicationContent() {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(AirliftService.class);
        return Lists.newArrayList(beanMap.values());
    }

    //todo
    private ServerConfig buildServerConfig() {
        return ServerConfig.builder()
                .withRegistryUrls(airliftProperties.getRegistryUrls())
                .withPort(airliftProperties.getServer().getPort())
                .build();
    }

}
