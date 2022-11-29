package com.dss.exception;

public class ActorCannotBeDeletedException extends RuntimeException {
    public ActorCannotBeDeletedException() {super("Actor cannot be deleted with existing movies");}
}
