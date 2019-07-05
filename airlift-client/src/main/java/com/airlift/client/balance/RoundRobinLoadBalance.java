package com.airlift.client.balance;

import com.airlift.registry.URL;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance implements LoadBalance {


    private AtomicInteger countInteger = new AtomicInteger(0);

    @Override
    public URL select(List<URL> urls) {
        int size = urls.size();
        if (size == 1) {
            return urls.get(0);
        }
        int count = countInteger.get();
        if (count > urls.size()) {
            while (!countInteger.compareAndSet(count, 0)) {
                count = countInteger.get();
            }
        }
        int index = count % size;
        countInteger.incrementAndGet();
        return urls.get(index);
    }

}
