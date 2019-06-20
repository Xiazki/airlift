package com.airlift.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZooKeeperCuratorConnection {

    public CuratorFramework connection(String url) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(url).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        curatorFramework.start();
        return curatorFramework;
    }


}
