package com.airlift.client;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invocation<?> that = (Invocation<?>) o;
        return Arrays.equals(arguments, that.arguments) &&
                Objects.equals(method, that.method) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(clientProxyClass, that.clientProxyClass);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, methodName, clientProxyClass);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }
}
