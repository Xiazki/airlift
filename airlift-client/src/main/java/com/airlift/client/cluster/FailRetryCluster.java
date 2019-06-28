package com.airlift.client.cluster;

import com.airlift.client.Invocation;
import com.airlift.client.Invoker;
import com.airlift.client.balance.LoadBalance;
import com.airlift.client.exception.RPCException;
import com.airlift.registry.Registry;
import com.airlift.registry.RegistryFactoryProvider;
import com.airlift.registry.RegistryType;
import com.airlift.registry.URL;
import com.facebook.nifty.client.NiftyClientChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

public class FailRetryCluster extends AbstractPoolCluster {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private URL url;
    private LoadBalance loadBalance;
    private Registry registry;
    private int retry = 3;

    @Override
    public void connect(URL url) {
        if (!StringUtils.isEmpty(url.getRegistryUrls())) {
            registry = RegistryFactoryProvider.INSTANCE.getRegistryFactory(RegistryType.ZOOKEEPER).get(url);
        }
        this.url = url;
    }

    @Override
    public <T> Invoker<T> getInvoker() {
        return invocation -> {
            int count = retry;
            NiftyClientChannel channel = getPool().borrowObject(url);
            T client = thriftClientManager.createClient(channel, invocation.getClientProxy());
            while (count > 0) {
                try {
                    return invocation.getMethod().invoke(client, invocation.getArguments());
                } catch (Exception e) {
                    //todo 通过异常来处理连接池的连接
                    logger.warn("call {} err:{},retry:{}", invocation.getMethodName(), e.getMessage(), count);
                    count--;
                    if (count == 0) {
                        throw new RPCException("call " + invocation.getMethodName() + "failed ", e);
                    }
                }
            }
            throw new RPCException("call " + invocation.getMethodName() + "failed ");
        };
    }

    @Override
    public URL route() {
        if (registry == null) {
            return url;
        }
        //todo balance
        return loadBalance.select(registry.lookup());
    }

    @Override
    public Long getReadTimeout() {
        return null;
    }

    @Override
    public Long getConnectionTimeout() {
        return null;
    }

    @Override
    public Long getWriteTimeout() {
        return null;
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

    private interface RetryHelper {

        Object callWithRetry();

    }

    private class InvokerPoxyWapper implements Invoker {

        @Override
        public Object invoke(Invocation invocation) {
            return null;
        }

    }

}
