package com.airlift.springtest.api;

import com.airlift.springtest.api.module.ResultBean;
import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;

@ThriftService
public interface HelloWorldApi {

    @ThriftMethod
    ResultBean getHi(String name);

}
