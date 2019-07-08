package com.airlift.client.balance;

import com.airlift.client.Invocation;
import com.airlift.registry.URL;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance implements LoadBalance {

    private final ConcurrentHashMap<String, AtomicInteger> selectorMap = new ConcurrentHashMap<>();

    @Override
    public URL select(List<URL> urls, Invocation invocation) {
        int size = urls.size();
        if (size == 1) {
            return urls.get(0);
        }
        String key = urls.get(0).toKey();
        AtomicInteger counter = selectorMap.get(key);
        if (counter == null) {
            selectorMap.putIfAbsent(key, new AtomicInteger(0));
            counter = selectorMap.get(key);
        }
        int count = counter.getAndIncrement();
        int index = count % size;
        return urls.get(index);
    }

}
