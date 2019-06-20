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
        return this;
    }
}
