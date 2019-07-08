package com.airlift.consumer;

import com.airlift.client.balance.LoadBalance;
import com.airlift.client.balance.RoundRobinLoadBalance;
import com.airlift.registry.URL;

import java.util.ArrayList;
import java.util.List;

public class LoadBalanceTest {

    public static void main(String[] args) {
        LoadBalance loadBalance = new RoundRobinLoadBalance();

        List<URL> urls = new ArrayList<>();
        urls.add(URL.builder().withPort(1).build());
        urls.add(URL.builder().withPort(2).build());
        urls.add(URL.builder().withPort(3).build());
        urls.add(URL.builder().withPort(4).build());
        urls.add(URL.builder().withPort(5).build());
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                URL url = loadBalance.select(urls);
                System.out.println(Thread.currentThread().getName() + ":" + url.getPort());
            }).start();
        }

    }

}
