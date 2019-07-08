package com.airlift.client.cluster;

import com.airlift.client.Invocation;
import com.airlift.client.Invoker;
import com.airlift.registry.URL;

public interface Cluster {

    void connect(URL url);

    <T> Invoker<T>  getInvoker();

    URL route(Invocation invocation);

}
