package com.airlift.client.balance;

import com.airlift.registry.URL;

import java.util.List;
import java.util.Random;

public class SimpleRandomLoadBalance implements LoadBalance {
    @Override
    public URL select(List<URL> urls) {
        return urls.get(new Random().nextInt(urls.size()));
    }
}
