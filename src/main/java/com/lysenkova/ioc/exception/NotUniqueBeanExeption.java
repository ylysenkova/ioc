package com.lysenkova.ioc.exception;

public class NotUniqueBeanExeption extends RuntimeException {
    public NotUniqueBeanExeption(String message) {
        super(message);
    }

    public NotUniqueBeanExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
