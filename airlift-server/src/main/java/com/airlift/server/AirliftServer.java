package com.airlift.server;

import com.airlift.registry.*;
import com.airlift.server.config.ServerConfig;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftEventHandler;
import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.ThriftServerConfig;
import com.facebook.swift.service.ThriftServiceProcessor;
import com.xiazki.airlift.common.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AirliftServer implements Closeable {

    Logger logger = LoggerFactory.getLogger(AirliftServer.class);

    private List<Object> services;
    private ThriftServer thriftServer;
    private ServerConfig serverConfig;
    private List<ThriftEventHandler> thriftEventHandlers;
    private ThriftCodecManager thriftCodecManager;
    private Executor executor = Executors.newFixedThreadPool(2);
    private AbstractRegistryFactory registryFactory;
    private List<URL> providerUrls = new ArrayList<>();

    public AirliftServer() {
        serverConfig = new ServerConfig();
    }

    public AirliftServer(int port) {
        serverConfig = new ServerConfig();
        serverConfig.setPort(port);
    }

    public void start() {
        prepare();
        export();
        registry();
    }

    @Override
    public void close() {
        thriftServer.close();
        new Offline().off();
    }


    private void export() {
//        executor.execute(() -> {
        try {
            logger.info("{} - server begin to start...", serverConfig.getApplicationName());
            thriftServer.start();
            logger.info("server start at {},port:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), serverConfig.getPort());
        } catch (Exception e) {
            logger.error("failed to start server ", e);
            throw e;
        }
//        });
    }

    private void registry() {
        if (registryFactory != null) {
            providerUrls.forEach(url -> {
                Registry registry = registryFactory.get(url);
                registry.doRegister();
            });
            Runtime.getRuntime().addShutdownHook(new Thread(() -> new Offline().off()));
        }
    }

    private void prepare() {
        if (services == null) {
            services = new ArrayList<>();
        }
        if (thriftCodecManager == null) {
            thriftCodecManager = new ThriftCodecManager();
        }
        if (thriftEventHandlers == null) {
            thriftEventHandlers = new ArrayList<>();
        }
        ThriftServiceProcessor processor = new ThriftServiceProcessor(thriftCodecManager, thriftEventHandlers, services);
        if (thriftServer == null) {
            ThriftServerConfig thriftServerConfig = buildThriftConfig();
            thriftServer = thriftServerConfig == null ? new ThriftServer(processor) : new ThriftServer(processor, thriftServerConfig);
        }
        if (serverConfig.isNeedRegistry() && serverConfig.getRegistryUrls() != null) {
            this.registryFactory = RegistryFactoryProvider.INSTANCE.getRegistryFactory(RegistryType.ZOOKEEPER);
            services.forEach(o -> providerUrls.add(createUrl(o.getClass().getName())));
        }
    }


    private ThriftServerConfig buildThriftConfig() {
        if (serverConfig.getPort() == 0) {
            return null;
        }
        ThriftServerConfig thriftServerConfig = new ThriftServerConfig();
        thriftServerConfig.setPort(serverConfig.getPort());
        return thriftServerConfig;
    }

    private URL createUrl(String serviceInterface) {
        URL url = new URL();
        url.setPort(serverConfig.getPort());
        url.setHost(IPUtil.getIp());
        url.setRegistryUrls(serverConfig.getRegistryUrls());
        url.setServiceInterface(serviceInterface);
        url.setApplicationName(serverConfig.getApplicationName());
        url.setRegistryUrls(serverConfig.getRegistryUrls());
        return url;
    }

    public List<Object> getServices() {
        return services;
    }

    public void setServices(List<Object> services) {
        this.services = services;
    }

    public ThriftServer getThriftServer() {
        return thriftServer;
    }

    public void setThriftServer(ThriftServer thriftServer) {
        this.thriftServer = thriftServer;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public List<ThriftEventHandler> getThriftEventHandlers() {
        return thriftEventHandlers;
    }

    public void setThriftEventHandlers(List<ThriftEventHandler> thriftEventHandlers) {
        this.thriftEventHandlers = thriftEventHandlers;
    }

    public ThriftCodecManager getThriftCodecManager() {
        return thriftCodecManager;
    }

    public void setThriftCodecManager(ThriftCodecManager thriftCodecManager) {
        this.thriftCodecManager = thriftCodecManager;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

}
