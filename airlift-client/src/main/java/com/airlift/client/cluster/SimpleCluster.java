package com.airlift.client.cluster;

import com.airlift.client.Invocation;
import com.airlift.client.Invoker;
import com.airlift.registry.URL;

public class SimpleCluster implements Cluster{
    @Override
    public void connect(URL url) {

    }

    @Override
    public <T> Invoker<T>  getInvoker() {
        return null;
    }

    @Override
    public URL route(Invocation invocation) {
        return null;
    }
}
