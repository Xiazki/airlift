package com.airlift.client.cluster;

import com.airlift.client.Invocation;
import com.airlift.client.Invoker;
import com.airlift.client.balance.LoadBalance;
import com.airlift.client.balance.SimpleRandomLoadBalance;
import com.airlift.client.exception.RPCException;
import com.airlift.registry.Registry;
import com.airlift.registry.RegistryFactoryProvider;
import com.airlift.registry.RegistryType;
import com.airlift.registry.URL;
import com.facebook.nifty.client.NiftyClientChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;


public class FailRetryCluster extends AbstractPoolCluster {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private URL url;
    private LoadBalance loadBalance = new SimpleRandomLoadBalance();
    private Registry registry;
    private int retry = 3;

    @Override
    public void connect(URL url) {
        if (url.getRegistryUrls() != null && !url.getRegistryUrls().trim().equals("")) {
            registry = RegistryFactoryProvider.INSTANCE.getRegistryFactory(RegistryType.ZOOKEEPER).get(url);
            registry.subscribe();
        }
        this.url = url;
    }

    @Override
    public <T> Invoker<T> getInvoker() {
        return invocation -> {
            URL selectedUrl = route(invocation);
            NiftyClientChannel channel = getPool().borrowObject(selectedUrl);
            if (channel == null) {
                throw new RPCException("call " + invocation.getMethodName() + " failed,connection is closed");
            }
            T client = thriftClientManager.createClient(channel, invocation.getClientProxy());
            return callWithRetry(() -> invocation.getMethod().invoke(client, invocation.getArguments()), invocation.getMethodName());
        };
    }


    @Override
    public URL route(Invocation invocation) {
        if (registry == null) {
            return url;
        }
        return loadBalance.select(registry.lookup());
    }

    private Object callWithRetry(RetryHelper retryHelper, String methodName) {
        int count = retry;
        while (count > 0) {
            try {
                return retryHelper.invoke();
            } catch (Exception e) {
                //todo 通过异常来处理连接池中可能失效的的连接
                logger.warn("call {} failed err:{},retry:{}", methodName, e.getMessage(), count);
                count--;
                if (count == 0) {
                    throw new RPCException("call " + methodName + " failed", e);
                }
            }
        }
        throw new RPCException("call " + methodName + " failed");
    }

    @FunctionalInterface
    private interface RetryHelper {

        Object invoke() throws Exception;

    }


    private class InvokerPoxyWapper implements Invoker {

        @Override
        public Object invoke(Invocation invocation) {
            return null;
        }

    }


    public LoadBalance getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
