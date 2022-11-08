package com.dss.exception;

import java.util.UUID;

public class ReviewNotFoundException extends RuntimeException{

    public ReviewNotFoundException(UUID id) {
        super("Review not found with Id: ".concat(id.toString()));}
}
