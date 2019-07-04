package com.airlift.api.module;

import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.codec.ThriftStruct;

@ThriftStruct
public class ResultBean {

    private String message;
    private String code;

    @ThriftField(1)
    public String getMessage() {
        return message;
    }

    @ThriftField
    public void setMessage(String message) {
        this.message = message;
    }

    @ThriftField(2)
    public String getCode() {
        return code;
    }

    @ThriftField
    public void setCode(String code) {
        this.code = code;
    }


}
