package com.airlift.consumer;

import com.airlift.provider.HelloWorldApiService;
import com.airlift.server.AirliftServer;
import com.airlift.server.config.ServerConfig;

import java.util.ArrayList;
import java.util.List;

public class ServerStartTest {

    public static void main(String[] args){
        AirliftServer airliftServer = new AirliftServer();
        List<Object> services = new ArrayList<>();
        services.add(new HelloWorldApiService());
        airliftServer.setServices(services);
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(9013);
        serverConfig.setApplicationName("test");
        serverConfig.setNeedRegistry(true);
        serverConfig.setRegistryUrls("127.0.0.1:2181");
        airliftServer.setServerConfig(serverConfig);
        airliftServer.start();
        airliftServer.close();
    }

}
