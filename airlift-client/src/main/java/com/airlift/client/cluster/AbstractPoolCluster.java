package com.airlift.client.cluster;

import com.airlift.client.ThriftClientManagerHolder;
import com.airlift.registry.URL;
import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.nifty.client.NiftyClientChannel;
import com.facebook.nifty.client.NiftyClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.net.HostAndPort;
import com.google.common.util.concurrent.ListenableFuture;
import io.airlift.units.Duration;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class AbstractPoolCluster implements Cluster {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Duration connectionTimeout = new Duration(2000, TimeUnit.MILLISECONDS);
    protected Duration receiveTimeout = new Duration(1, TimeUnit.MINUTES);
    protected Duration readTimeout = new Duration(2000, TimeUnit.MILLISECONDS);
    protected Duration writeTimeout = new Duration(1, TimeUnit.MINUTES);
    public static final int DEFAULT_MAX_FRAME_SIZE = 16777216;


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
            HostAndPort hostAndPort = HostAndPort.fromParts(key.getHost(), key.getPort());
            NiftyClientConnector connector = new FramedClientConnector(hostAndPort);
            ListenableFuture listenableFuture = thriftClientManager.createChannel(connector, connectionTimeout, receiveTimeout, readTimeout, writeTimeout, DEFAULT_MAX_FRAME_SIZE, null);
            return (NiftyClientChannel) listenableFuture.get(connectionTimeout.toMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public PooledObject<NiftyClientChannel> wrap(NiftyClientChannel value) {
            return new DefaultPooledObject<>(value);
        }

        @Override
        public boolean validateObject(URL key, PooledObject<NiftyClientChannel> p) {
            Channel channel = p.getObject().getNettyChannel();
            return channel.isOpen();
        }

        @Override
        public void destroyObject(URL key, PooledObject<NiftyClientChannel> p) throws Exception {
            Channel channel = p.getObject().getNettyChannel();
            if (channel != null) {
                channel.close();
            }
            super.destroyObject(key, p);
        }
    }


    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Duration getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(Duration receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
