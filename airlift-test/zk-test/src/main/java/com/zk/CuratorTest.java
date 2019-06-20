package com.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorTest {

    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1").sessionTimeoutMs(200)
                .retryPolicy(new ExponentialBackoffRetry(10, 5))
                .build();
        curatorFramework.start();

        Thread.sleep(7000);

        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/airlift");


    }

}
