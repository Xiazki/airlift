# airlift
airlift是一个基于thrift的rpc服务框架，利用[Swift](https://github.com/facebookarchive/swift)(停止维护了:pensive:)提供的注解来创建简单可序列化的thrift类型和服务。利用zookeeper提供了动态的注册和服务发现
并增加了客户端的负载均衡、重试机制。


## Getting started

**一个简单的例子**

定义一个 `ThriftStruct` 参考 [Swift Codec](https://github.com/facebookarchive/swift/tree/master/swift-codec).
```java
@ThriftStruct
public class ResultBean {

    private String message;
    private String code;

    @ThriftField(1)
    public String getMessage() {
        return message;
    }

    @ThriftField
    public void setMessage(String message) {
        this.message = message;
    }

    @ThriftField(2)
    public String getCode() {
        return code;
    }

    @ThriftField
    public void setCode(String code) {
        this.code = code;
    }
}
```
使用 `ThriftService` 定义一个提供服务的接口，并实现这个接口，如下：
```java
@ThriftService
public interface HelloWorldApi {

    @ThriftMethod
    ResultBean getHi(String name);

}
```
接口实现类：
```java
public class HelloWorldApiService implements HelloWorldApi {

    public ResultBean getHi(String name) {
        System.out.println("call getHi");
        ResultBean resultBean = new ResultBean();
        resultBean.setMessage(name + " hello world!");
        resultBean.setCode("0");
        return resultBean;
    }
}

```

使用`AirliftServer`启动服务，服务监听9013，`withRegistryUrls("127.0.0.1:2181")` 注册zookeeper地址,多个地址用`;`拼接。
使用`AirliftClientFactory`来创建客户端代理。
```java
public class ServerStartTest {
    public static void main(String[] args) {
        List<Object> services = new ArrayList<>();
        services.add(new HelloWorldApiService());

        ServerConfig serverConfig = ServerConfig.builder().withPort(9013).withRegistryUrls("127.0.0.1:2181").build();
        ClientConfig clientConfig = ClientConfig.builer().withRegistryUrls("127.0.0.1:2181").build();
        try (
                AirliftServer airliftServer = new AirliftServer(serverConfig, services).start();
                AirliftClientFactory<HelloWorldApi> clientFactory = new AirliftClientFactory<>(clientConfig)
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
