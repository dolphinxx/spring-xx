package com.jagsii.springxx.common.oss;

public class OSSException extends RuntimeException {
    public OSSException() {
    }

    public OSSException(String message) {
        super(message);
    }

    public OSSException(String message, Throwable cause) {
        super(message, cause);
    }
}
