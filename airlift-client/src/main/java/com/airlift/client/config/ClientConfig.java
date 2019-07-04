package com.airlift.client.config;

import io.airlift.units.Duration;

import java.util.concurrent.TimeUnit;

public class ClientConfig {

//    public static Duration DEFAULTCONNECTIONTIMEOUT = new Duration(2000, TimeUnit.MILLISECONDS);
//    public static Duration DEFAULTRECEIVETIMEOUT = new Duration(1, TimeUnit.MINUTES);
//    public static Duration DEFAULTREADTIMEOUT = new Duration(2000, TimeUnit.MILLISECONDS);
//    public static Duration DEFAULTWRITETIMEOUT = new Duration(1, TimeUnit.MINUTES);

    private String host;
    private int port;
    private boolean needRegistry;
    private String registryUrls;
    private String applicationName;

    private Long readTimeout;
    private Long ReceiveTimeout;
    private Long writeTimeout;
    private Long connectionTimeout;

    private int retry;


    public static ClientConfigBuilder builer() {
        return new ClientConfigBuilder();
    }

    public static final class ClientConfigBuilder {

        private String host;
        private int port;
        private boolean needRegistry;
        private String registryUrls;
        private Long readTimeout;
        private Long ReceiveTimeout;
        private Long writeTimeout;
        private Long connectionTimeout;
        private int retry;

        private ClientConfigBuilder() {
        }

        public ClientConfigBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public ClientConfigBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public ClientConfigBuilder withNeedRegistry(boolean needRegistry) {
            this.needRegistry = needRegistry;
            return this;
        }

        public ClientConfigBuilder withRegistryUrls(String registryUrls) {
            this.registryUrls = registryUrls;
            return this;
        }

        public ClientConfigBuilder withReadTimeout(Long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public ClientConfigBuilder withReceiveTimeout(Long ReceiveTimeout) {
            this.ReceiveTimeout = ReceiveTimeout;
            return this;
        }

        public ClientConfigBuilder withWriteTimeout(Long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public ClientConfigBuilder withConnectionTimeout(Long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public ClientConfigBuilder withRetry(int retry) {
            this.retry = retry;
            return this;
        }

        public ClientConfig build() {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.connectionTimeout = this.connectionTimeout;
            clientConfig.port = this.port;
            clientConfig.registryUrls = this.registryUrls;
            clientConfig.retry = this.retry;
            clientConfig.ReceiveTimeout = this.ReceiveTimeout;
            clientConfig.readTimeout = this.readTimeout;
            clientConfig.host = this.host;
            clientConfig.needRegistry = this.needRegistry;
            clientConfig.writeTimeout = this.writeTimeout;
            return clientConfig;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isNeedRegistry() {
        return needRegistry;
    }

    public void setNeedRegistry(boolean needRegistry) {
        this.needRegistry = needRegistry;
    }

    public String getRegistryUrls() {
        return registryUrls;
    }

    public void setRegistryUrls(String registryUrls) {
        this.registryUrls = registryUrls;
    }

    public Long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Long getReceiveTimeout() {
        return ReceiveTimeout;
    }

    public void setReceiveTimeout(Long receiveTimeout) {
        ReceiveTimeout = receiveTimeout;
    }

    public Long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
