package com.airlift.registry.exception;

public class RegistryException extends RuntimeException {

    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
