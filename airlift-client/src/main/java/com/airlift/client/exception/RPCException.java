package com.airlift.client.exception;


public class RPCException extends RuntimeException {

    public RPCException(String message) {
        super(message);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }
}
