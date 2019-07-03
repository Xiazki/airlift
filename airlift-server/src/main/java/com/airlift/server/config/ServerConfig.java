package com.airlift.server.config;

public class ServerConfig {

    private String applicationName;
    private int port;
    private boolean needRegistry;
    private String registryUrls;

    public String getApplicationName() {
        return applicationName;
    }

    public ServerConfig setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ServerConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public boolean isNeedRegistry() {
        return needRegistry;
    }

    public ServerConfig setNeedRegistry(boolean needRegistry) {
        this.needRegistry = needRegistry;
        return this;
    }

    public String getRegistryUrls() {
        return registryUrls;
    }

    public ServerConfig setRegistryUrls(String registryUrls) {
        this.registryUrls = registryUrls;
        this.needRegistry = true;
        return this;
    }


    public static ServerConfigBuilder builder() {
        return new ServerConfigBuilder();
    }

    public static final class ServerConfigBuilder {
        private String applicationName ="airlift";
        private int port;
        private boolean needRegistry;
        private String registryUrls;

        private ServerConfigBuilder() {
        }


        public ServerConfigBuilder withApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public ServerConfigBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public ServerConfigBuilder withNeedRegistry(boolean needRegistry) {
            this.needRegistry = needRegistry;
            return this;
        }

        public ServerConfigBuilder withRegistryUrls(String registryUrls) {
            this.registryUrls = registryUrls;
            this.needRegistry = true;
            return this;
        }

        public ServerConfig build() {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.setApplicationName(applicationName);
            serverConfig.setPort(port);
            serverConfig.setNeedRegistry(needRegistry);
            serverConfig.setRegistryUrls(registryUrls);
            return serverConfig;
        }

    }
}
