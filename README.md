# airlift
--基于thrift的rpc服务框架


## Getting started

**一个简单的例子**

利用`AirliftServer`启动服务

```java

import com.airlift.provider.HelloWorldApiService;
import com.airlift.server.AirliftServer;
import com.airlift.server.config.ServerConfig;

import java.util.ArrayList;
import java.util.List;

public class ServerStartTest {

    public static void main(String[] args) {

        List<Object> services = new ArrayList<>();
        services.add(new HelloWorldApiService());

        AirliftServer airliftServer = new AirliftServer(ServerConfig.builder()
                .withPort(9013)
                .withRegistryUrls("127.0.0.1:2181")
                .build());
        
        airliftServer.setServices(services);

        airliftServer.start();

    }

}

```