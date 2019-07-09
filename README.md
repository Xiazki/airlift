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

## 在springboot中使用airlift

将项目clone下来后，使用maven打包到仓库后引入依赖
```
  
        <dependency>
            <groupId>com.xiazki</groupId>
            <artifactId>airlift-springboot-starter</artifactId>
            <version>1.0</version>
        </dependency>
        
```
在启动类上使用 `EnableAirlift` 来引入自动化配置，并且在application.yml配置文件中添加如下参数
```java
@SpringBootApplication
@EnableAirlift
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
```
yml中属性：
```yaml
airlift:
  registry-urls: 127.0.0.1:2181
  application-name: airlift
  client:
    balance: "round-robin"
    cluster: "fail-retry"
  server:
    enable: true
    port: 9013
```
（配置参数还在完善中）

完成这些后，使用 `AirliftClient` 注解来注入airlift 服务依赖, 使用 `AirliftService`注解注册一个thrift rpc服务。

客户端
```java
@RestController
@RequestMapping("/test")
public class TestController {

    @AirliftClient
    private HelloWorldApi helloWorldApi;

    @GetMapping
    public String get(@RequestParam("name") String name){
        return helloWorldApi.getHi(name).getMessage();
    }

}
```
服务端
```java

@AirliftService
public class HelloWorldApiService implements HelloWorldApi {
    public ResultBean getHi(String name) {
        ResultBean resultBean = new ResultBean();
        resultBean.setMessage(name + ", hello!");
        resultBean.setCode("0");
        return resultBean;
    }
}

```

目前还要许多的功能和bug在不断完善和优化中。