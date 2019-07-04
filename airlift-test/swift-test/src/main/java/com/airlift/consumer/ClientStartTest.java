package com.airlift.consumer;

import com.airlift.api.HelloWorldApi;
import com.airlift.api.module.ResultBean;
import com.airlift.client.AirliftClientFactory;
import com.airlift.client.config.ClientConfig;

public class ClientStartTest {

    public static void main(String[] args) {
        ClientConfig clientConfig = ClientConfig.builer().withPort(9013).withRegistryUrls("127.0.0.1:2181").withHost("127.0.0.1").build();
        AirliftClientFactory<HelloWorldApi> clientFactory = new AirliftClientFactory<>(HelloWorldApi.class, clientConfig);
        HelloWorldApi helloWorldApi = clientFactory.get();
        ResultBean resultBean = helloWorldApi.getHi("test");
        System.out.println(resultBean.getMessage());
    }

}
