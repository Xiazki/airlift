package com.airlift.registry;

import com.airlift.registry.zookeeper.ZooKeeperRegistryFactory;

import java.util.HashMap;

public class RegistryFactoryProvider {

    private HashMap<RegistryType, AbstractRegistryFactory> factorys = new HashMap<>();
    public final static RegistryFactoryProvider INSTANCE = new RegistryFactoryProvider();

    private RegistryFactoryProvider() {
        factorys.put(RegistryType.ZOOKEEPER, new ZooKeeperRegistryFactory());
    }

    public AbstractRegistryFactory getRegistryFactory(RegistryType registryType) {
        return factorys.get(registryType);
    }


}
