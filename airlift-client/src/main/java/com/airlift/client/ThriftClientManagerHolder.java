package com.airlift.client;

import com.facebook.swift.service.ThriftClientManager;

/**
 * 初始化thrift client Manger 后续修改
 * todo
 */
public class ThriftClientManagerHolder {

    private final static ThriftClientManager THRIFT_CLIENT_MANAGER = new ThriftClientManager();

    public static ThriftClientManager getThriftClientManager() {
        return THRIFT_CLIENT_MANAGER;
    }

}
