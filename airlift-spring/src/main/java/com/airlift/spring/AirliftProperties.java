package com.airlift.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "airlift")
public class AirliftProperties {

    private String applicationName = "airlift";
    private Long readTimeout;
    private Long ReceiveTimeout;
    private Long writeTimeout;
    private Long connectionTimeout;
    private String registryUrls;

    private ClientProperties client;
    private ServerProperties server;


    public static class ClientProperties {
        private String balance;
        private String cluster;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCluster() {
            return cluster;
        }

        public void setCluster(String cluster) {
            this.cluster = cluster;
        }
    }

    public static class ServerProperties {
        private boolean enable = false;
        private int port = 9013;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

    public String getRegistryUrls() {
        return registryUrls;
    }

    public void setRegistryUrls(String registryUrls) {
        this.registryUrls = registryUrls;
    }

    public ClientProperties getClient() {
        return client;
    }

    public void setClient(ClientProperties client) {
        this.client = client;
    }

    public ServerProperties getServer() {
        return server;
    }

    public void setServer(ServerProperties server) {
        this.server = server;
    }
}
