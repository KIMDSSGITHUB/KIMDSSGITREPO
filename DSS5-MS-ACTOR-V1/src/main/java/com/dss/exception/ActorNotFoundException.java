package com.dss.exception;

import java.util.UUID;

public class ActorNotFoundException extends RuntimeException{

    public ActorNotFoundException(UUID id) {
        super("Actor not found with Id: ".concat(id.toString()));}
}
