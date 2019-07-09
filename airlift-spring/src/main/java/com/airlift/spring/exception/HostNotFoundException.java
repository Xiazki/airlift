package com.airlift.spring.exception;

public class HostNotFoundException extends RuntimeException{
    public HostNotFoundException() {
    }

    public HostNotFoundException(String message) {
        super(message);
    }
}
