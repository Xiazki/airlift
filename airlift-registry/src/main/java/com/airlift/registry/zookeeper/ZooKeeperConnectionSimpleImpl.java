package com.airlift.registry.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * todo 利用适配器优化，实现多个链接方案
 */
public class ZooKeeperConnectionSimpleImpl {

    private Logger logger = LoggerFactory.getLogger(ZooKeeperConnectionSimpleImpl.class);

    //    private ZooKeeper zooKeeper;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public ZooKeeper connection(final String host) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper(host, 5000, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        countDownLatch.countDown();
                        logger.info("success to connect zookeeper host:{}", host);
                    }
                }
            });
            countDownLatch.await();
            return zooKeeper;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("connect to zookeeper failed,host:{},err:{}", host, e);
        } catch (InterruptedException e) {
            logger.error("an interruption occurred while waiting for a connection host:{},err:{}", host, e);
            e.printStackTrace();
        }
        return null;
    }

}
