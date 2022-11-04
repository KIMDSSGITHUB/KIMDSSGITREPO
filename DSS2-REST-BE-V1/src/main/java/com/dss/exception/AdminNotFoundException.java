package com.dss.exception;

public class AdminNotFoundException extends RuntimeException{

    private static final long serialVersionUID = -2981233739055786578L;

    public AdminNotFoundException(String email) {
        super("Admin not found: " + email);
    }
}
