package com.airlift.registry;

import java.util.List;

//todo subscribe 订阅register ulr
public interface Registry {

    void doRegister();

    void doUnRegister();

    List<URL> lookup();

    void destroy();
}
