package com.airlift.client;

import com.airlift.client.cluster.Cluster;
import com.airlift.client.cluster.FailRetryCluster;
import com.airlift.client.config.ClientConfig;
import com.airlift.client.exception.RPCException;
import com.airlift.registry.URL;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;

public class AirliftClientFactory<T> implements Closeable {

    private Cluster cluster;
    private ClientConfig clientConfig;
    private URL url;
    private Class<T> interfaceClass;
    private volatile T client;

    public AirliftClientFactory(Class<T> interfaceClass, ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.interfaceClass = interfaceClass;
    }

    public T get() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = create();
                }
            }
        }
        return client;
    }

    private T create() {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("interfaceClass can not be null");
        }
        if (clientConfig == null) {
            throw new IllegalArgumentException("clientConfig can not be null");
        }
        try {
            initUrl();
            connectToCluster();
            return createJavassistProxy();
        } catch (Exception e) {
            throw new RPCException("init client failed,err:{}" + e.getMessage(), e);
        }
    }

    private T createJavassistProxy() throws IllegalAccessException, InstantiationException {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(new Class[]{interfaceClass});
        Class proxyClass = proxyFactory.createClass();
        T proxy = (T) proxyClass.newInstance();
        ((ProxyObject) proxy).setHandler(new ProxyHandler());
        return proxy;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public void close() throws IOException {
        ThriftClientManagerHolder.getThriftClientManager().close();
    }

    private class ProxyHandler implements MethodHandler {

        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            Invocation invocation = invocation(thisMethod, args);
            return cluster.getInvoker().invoke(invocation);
        }

    }

    private void initUrl() {
        this.url = URL.builder()
                .withApplicationName(clientConfig.getApplicationName())
                .withHost(clientConfig.getHost())
                .withPort(clientConfig.getPort())
                .withKeyValue("client")
                .withServiceInterface(interfaceClass.getName())
                .withRegistryUrls(clientConfig.getRegistryUrls())
                .build();
    }

    private void connectToCluster() {
        if (cluster == null) {
            cluster = new FailRetryCluster();
        }
        cluster.connect(url);
    }

    private Invocation<T> invocation(Method method, Object[] args) {
        Invocation<T> invocation = new Invocation<>();
        invocation.setArguments(args);
        invocation.setClientProxy(interfaceClass);
        invocation.setMethod(method);
        invocation.setMethodName(method.getName());
        return invocation;
    }
}
