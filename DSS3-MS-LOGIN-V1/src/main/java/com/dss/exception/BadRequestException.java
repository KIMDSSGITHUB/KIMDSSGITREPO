package com.dss.exception;

public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 4853232046330404671L;

    public BadRequestException(String message) {
        super(message);
    }
}
