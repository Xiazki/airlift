package com.airlift.client;

import com.airlift.client.cluster.Cluster;
import com.airlift.client.config.ClientConfig;

public class AirliftClientFactory<T> {

    private Cluster cluster;
    private ClientConfig clientConfig;

    public T get() {
        return null;
    }


}