package com.airlift.springtest.services;


import com.airlift.spring.annotation.AirliftService;
import com.airlift.springtest.api.HelloWorldApi;
import com.airlift.springtest.api.module.ResultBean;

@AirliftService
public class HelloWorldApiService implements HelloWorldApi {
    public ResultBean getHi(String name) {
        ResultBean resultBean = new ResultBean();
        resultBean.setMessage(name + ", hello!");
        resultBean.setCode("0");
        return resultBean;
    }
}
