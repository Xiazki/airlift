package com.airlift.registry;

import com.airlift.registry.exception.RegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractRegistryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegistryFactory.class);

    private static final Map<String, Registry> REGISTRIES = new ConcurrentHashMap<>();

    private static final ReentrantLock LOCK = new ReentrantLock();

    public abstract Registry create(URL url);

    public void distoryAll() {
        LOCK.lock();
        try {
            for (Registry registry : getRegistries()) {
                try {
                    registry.destroy();
                } catch (Throwable e) {
                    LOGGER.error("close registry failed :{}", e);
                }
            }
            REGISTRIES.clear();
        } finally {
            LOCK.unlock();
        }
    }

    public Registry get(URL url) {
        String key = url.getServiceInterface();
        LOCK.lock();
        try {
            Registry registry = REGISTRIES.get(key);
            if (registry != null) {
                return registry;
            }
            registry = create(url);
            if (registry == null) {
                throw new RegistryException("can not create registry " + url, null);
            }
            REGISTRIES.put(key, registry);
            return registry;
        } finally {
            LOCK.unlock();
        }
    }

    public Collection<Registry> getRegistries() {
        return REGISTRIES.values();
    }

}
