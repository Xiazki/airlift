package com.airlift.client.balance;

import com.airlift.client.Invocation;
import com.airlift.registry.URL;

import java.util.List;
import java.util.Random;

public class SimpleRandomLoadBalance implements LoadBalance {
    @Override
    public URL select(List<URL> urls, Invocation invocation) {
        return urls.get(new Random().nextInt(urls.size()));
    }
}
