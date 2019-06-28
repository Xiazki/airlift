package com.airlift.client;

import java.lang.reflect.Method;

public class Invocation<T> {

    private Object[] arguments;
    private Method method;
    private String methodName;
    private Class<T> clientProxyClass;

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<T> getClientProxy() {
        return clientProxyClass;
    }

    public void setClientProxy(Class<T> clientProxyClass) {
        this.clientProxyClass = clientProxyClass;
    }
}
