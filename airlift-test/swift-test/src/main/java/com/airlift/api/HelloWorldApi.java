package com.airlift.api;

import com.airlift.api.module.ResultBean;
import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;

@ThriftService
public interface HelloWorldApi {

    @ThriftMethod
    ResultBean getHi(String name);

}
