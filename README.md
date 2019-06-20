# airlift
--基于thrift的rpc服务框架


## Getting started

**服务启动**
利用AirliftServer启动服务

```java
package com.airlift.consumer;
import com.airlift.api.HelloWorldApi;
import com.airlift.provider.HelloWorldApiService;
import com.airlift.server.AirliftServer;
import com.airlift.server.config.ServerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerStartTest {

    public static void main(String[] args) throws InterruptedException, IOException {
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
```