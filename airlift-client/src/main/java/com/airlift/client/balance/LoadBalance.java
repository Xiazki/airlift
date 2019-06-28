package com.airlift.client.balance;

import com.airlift.registry.URL;

import java.util.List;

public interface LoadBalance {

    URL select(List<URL> urls);

}
