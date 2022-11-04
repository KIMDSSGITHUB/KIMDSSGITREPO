package com.dss.exception;

public class InvalidCredentialsException extends RuntimeException {

    private static final long serialVersionUID = 3818837426757950818L;

    public InvalidCredentialsException() {
        super("Email or password is incorrect");
    }
}
