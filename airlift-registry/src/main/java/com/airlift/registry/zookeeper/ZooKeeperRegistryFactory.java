package com.airlift.registry.zookeeper;

import com.airlift.registry.AbstractRegistryFactory;
import com.airlift.registry.Registry;
import com.airlift.registry.URL;

public class ZooKeeperRegistryFactory extends AbstractRegistryFactory {

    private ZooKeeperCuratorConnection zooKeeperCuratorConnection = new ZooKeeperCuratorConnection();

    public Registry create(URL url) {
        return new ZooKeeperRegistry(url, zooKeeperCuratorConnection);
    }

}
