package com.airlift.provider;

import com.airlift.api.HelloWorldApi;
import com.airlift.api.module.ResultBean;

public class HelloWorldApiService implements HelloWorldApi {

    public ResultBean getHi(String name) {

        System.out.println("call getHi");
        ResultBean resultBean = new ResultBean();
        resultBean.setMessage(name + " hello world!");
        resultBean.setCode("0");
        return resultBean;
//        return new ResultBean() {{
//            setCode("0");
//            setMessage(name + " hello world!");
//        }};
    }
}
