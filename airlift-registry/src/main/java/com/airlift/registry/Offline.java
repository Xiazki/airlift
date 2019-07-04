package com.airlift.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Offline {

    Logger logger = LoggerFactory.getLogger(Offline.class);

    private AbstractRegistryFactory registryFactory = RegistryFactoryProvider.INSTANCE.getRegistryFactory(RegistryType.ZOOKEEPER);

    public void off() {
        Collection<Registry> registries = registryFactory.getRegistries();
        if (registries != null && !registries.isEmpty()) {
            registries.forEach(registry -> {
                try {
                    registry.doUnRegister();
                } catch (Exception e) {
                    logger.error("do unregistry failed", e);
                }

            });
        }
        registryFactory.destroyAll();
    }

}
