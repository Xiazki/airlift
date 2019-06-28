package com.airlift.client;

public interface Invoker<T> {

    Object invoke(Invocation<T> invocation) throws Exception;

}
