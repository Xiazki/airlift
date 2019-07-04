package com.airlift.registry;

import java.util.Map;
import java.util.Objects;

public class URL {

    private String applicationName;
    private String host;
    private int port;
    private String registryUrls;
    private String serviceInterface;
    private String keyValue = "airlift";

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

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String toKey() {
        return serviceInterface + "#" + keyValue;
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

    public static URLBuilder builder() {
        return new URLBuilder();
    }

    public static final class URLBuilder {
        private String applicationName;
        private String host;
        private int port;
        private String registryUrls;
        private String serviceInterface;
        private String keyValue;

        private URLBuilder() {
        }


        public URLBuilder withApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public URLBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public URLBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public URLBuilder withRegistryUrls(String registryUrls) {
            this.registryUrls = registryUrls;
            return this;
        }

        public URLBuilder withServiceInterface(String serviceInterface) {
            this.serviceInterface = serviceInterface;
            return this;
        }

        public URLBuilder withKeyValue(String keyValue) {
            this.keyValue = keyValue;
            return this;
        }

        public URL build() {
            URL uRL = new URL();
            uRL.setApplicationName(applicationName);
            uRL.setHost(host);
            uRL.setPort(port);
            uRL.setRegistryUrls(registryUrls);
            uRL.setServiceInterface(serviceInterface);
            uRL.setKeyValue(keyValue);
            return uRL;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return port == url.port &&
                Objects.equals(applicationName, url.applicationName) &&
                Objects.equals(host, url.host) &&
                Objects.equals(registryUrls, url.registryUrls) &&
                Objects.equals(serviceInterface, url.serviceInterface) &&
                Objects.equals(keyValue, url.keyValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationName, host, port, registryUrls, serviceInterface, keyValue);
    }
}
