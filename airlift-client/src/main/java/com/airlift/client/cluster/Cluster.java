package com.airlift.client.cluster;

import com.airlift.client.Invoker;
import com.airlift.registry.URL;

import java.util.List;

public interface Cluster {

    void connect(URL url);

    <T> Invoker<T>  getInvoker();

    URL route();

}
