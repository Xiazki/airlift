package com.airlift.consumer;

import com.airlift.api.HelloWorldApi;
import com.airlift.api.module.ResultBean;
import com.airlift.client.AirliftClientFactory;
import com.airlift.client.config.ClientConfig;
import com.airlift.provider.HelloWorldApiService;
import com.airlift.server.AirliftServer;
import com.airlift.server.config.ServerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerStartTest {

    public static void main(String[] args) {
        List<Object> services = new ArrayList<>();
        services.add(new HelloWorldApiService());
        ServerConfig serverConfig = ServerConfig.builder().withPort(9013).withRegistryUrls("127.0.0.1:2181").build();
        new AirliftServer(serverConfig, services).start();
    }

}
