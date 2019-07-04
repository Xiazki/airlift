# airlift
--基于thrift的rpc服务框架


## Getting started

**一个简单的例子**

利用`AirliftServer`启动服务，服务监听9013，`withRegistryUrls("127.0.0.1:2181")` 注册zookeeper地址,多个地址用`;`拼接。
使用`AirliftClientFactory`来创建客户端代理。
```java

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
            ResultBean resultBean =  helloWorldApi.getHi("test");
            System.out.println(resultBean.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```
