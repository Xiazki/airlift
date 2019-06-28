package com.airlift.client.cluster;

import com.airlift.client.Invoker;
import com.airlift.client.ThriftClientManagerHolder;
import com.airlift.registry.URL;
import com.facebook.nifty.client.NiftyClientChannel;
import com.facebook.swift.service.ThriftClientManager;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPoolCluster implements Cluster {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Long readTimeout;
    protected Long connectionTimeout;
    protected Long writeTimeout;
    protected GenericKeyedObjectPool<URL, NiftyClientChannel> pool;
    protected final ThriftClientManager thriftClientManager = ThriftClientManagerHolder.getThriftClientManager();

    AbstractPoolCluster() {
        this.pool = new GenericKeyedObjectPool<>(new ChannelPoolObjectFactory(), new GenericKeyedObjectPoolConfig<>());
    }


    public GenericKeyedObjectPool<URL, NiftyClientChannel> getPool() {
        return pool;
    }

    public void setPool(GenericKeyedObjectPool<URL, NiftyClientChannel> pool) {
        this.pool = pool;
    }

    private class ChannelPoolObjectFactory extends BaseKeyedPooledObjectFactory<URL, NiftyClientChannel> {


        @Override
        public NiftyClientChannel create(URL key) throws Exception {

            return null;
        }

        @Override
        public PooledObject<NiftyClientChannel> wrap(NiftyClientChannel value) {
            return null;
        }

        @Override
        public boolean validateObject(URL key, PooledObject<NiftyClientChannel> p) {
            return super.validateObject(key, p);
        }

        @Override
        public void destroyObject(URL key, PooledObject<NiftyClientChannel> p) throws Exception {
            super.destroyObject(key, p);
        }
    }

    public abstract Long getReadTimeout();

    public abstract Long getConnectionTimeout();

    public abstract Long getWriteTimeout();

}
