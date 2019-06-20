package com.airlift.registry;

public class URL {

    private String applicationName;
    private String host;
    private int port;
    private String registryUrls;
    private String serviceInterface;


    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

    public String getRegistryUrls() {
        return registryUrls;
    }

    public void setRegistryUrls(String registryUrls) {
        this.registryUrls = registryUrls;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public String toString() {
        return "URL{" +
                "applicationName='" + applicationName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", registryUrls='" + registryUrls + '\'' +
                ", serviceInterface='" + serviceInterface + '\'' +
                '}';
    }
}
