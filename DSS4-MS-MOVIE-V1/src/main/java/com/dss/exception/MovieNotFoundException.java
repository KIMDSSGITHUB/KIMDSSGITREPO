package com.dss.exception;

import java.util.UUID;

public class MovieNotFoundException extends RuntimeException{
    public MovieNotFoundException(UUID id) {
        super("Movie not found with Id: ".concat(id.toString()));}
}
