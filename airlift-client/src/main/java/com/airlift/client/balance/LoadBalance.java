package com.airlift.client.balance;

import com.airlift.client.Invocation;
import com.airlift.registry.URL;

import java.util.List;

public interface LoadBalance {

    /**
     * @param urls URL 列表
     * @return 选择出的URL
     */
    URL select(List<URL> urls, Invocation invocation);

}
