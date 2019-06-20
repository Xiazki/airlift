package com.airlift.consumer;

import com.airlift.api.HelloWorldApi;
import com.airlift.api.module.ResultBean;
import com.airlift.provider.HelloWorldApiService;
import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftClientManager;
import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.ThriftServiceProcessor;
import com.google.common.net.HostAndPort;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TestConsumer {

    public static void main(String[] args) {

        ThriftServiceProcessor processor = new ThriftServiceProcessor(new ThriftCodecManager(), new ArrayList<>(), new HelloWorldApiService());

//        ThriftServer server = new ThriftServer(processor).start();
//        ThriftServer server1 = new ThriftServer(processor).start();
//        ThriftServer server2 = new ThriftServer(processor).start();
        try (
                ThriftServer server = new ThriftServer(processor).start();
                ThriftServer server1 = new ThriftServer(processor).start();
                ThriftServer server2 = new ThriftServer(processor).start();
                ThriftClientManager thriftClientManager = new ThriftClientManager()

        ) {
            FramedClientConnector connector = new FramedClientConnector(HostAndPort.fromParts("localhost", server.getPort()));
            FramedClientConnector connector1 = new FramedClientConnector(HostAndPort.fromParts("localhost", server1.getPort()));
            FramedClientConnector connector2 = new FramedClientConnector(HostAndPort.fromParts("localhost", server2.getPort()));

            HelloWorldApi helloWorldApi = thriftClientManager.createClient(connector, HelloWorldApi.class).get();
            HelloWorldApi helloWorldApi1 = thriftClientManager.createClient(connector, HelloWorldApi.class).get();
            HelloWorldApi helloWorldApi2 = thriftClientManager.createClient(connector, HelloWorldApi.class).get();

            ResultBean resultBean = helloWorldApi.getHi("xiaxiang");
            ResultBean resultBean1 = helloWorldApi1.getHi("xiaxiang1");
            ResultBean resultBean2 = helloWorldApi2.getHi("xiaxiang2");
            System.out.println(resultBean.getMessage());
            System.out.println(resultBean1.getMessage());
            System.out.println(resultBean2.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
