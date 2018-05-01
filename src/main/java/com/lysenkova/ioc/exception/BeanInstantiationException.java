package com.lysenkova.ioc.exception;

public class BeanInstantiationException extends RuntimeException {
    public BeanInstantiationException() {
    }

    public BeanInstantiationException(String message) {
        super(message);
    }

    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
