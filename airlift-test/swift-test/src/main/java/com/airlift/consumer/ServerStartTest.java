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
        ClientConfig clientConfig = ClientConfig.builer().withPort(9013).withRegistryUrls("127.0.0.1:2181").withHost("127.0.0.1").build();
        try (
                AirliftServer airliftServer = new AirliftServer(serverConfig, services).start();
                AirliftClientFactory<HelloWorldApi> clientFactory = new AirliftClientFactory<>(clientConfig);

        ) {

            HelloWorldApi helloWorldApi = clientFactory.get();
            ResultBean resultBean = helloWorldApi.getHi("test");
            System.out.println(resultBean.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
