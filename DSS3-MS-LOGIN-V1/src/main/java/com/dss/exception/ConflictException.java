package com.dss.exception;

public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 4853232046330404671L;

    public ConflictException(String message) {
        super(message);
    }
}
