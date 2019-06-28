package com.airlift.client.config;

public class ClientConfig {

    private String host;
    private int port;
    private boolean needRegistry;
    private String registryUrls;

    private Long readTimeout;
    private Long ReceiveTimeout;
    private Long writeTimeout;
    private Long connectionTimeout;

    private int retry;


}
