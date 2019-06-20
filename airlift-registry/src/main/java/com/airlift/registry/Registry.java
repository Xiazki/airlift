package com.airlift.registry;

import java.util.List;

public interface Registry {

    void doRegister();

    void doUnRegister();

    List<URL> lookup();

    void destroy();
}
