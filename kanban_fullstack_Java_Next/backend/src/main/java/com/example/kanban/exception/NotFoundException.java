package com.example.kanban.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String name, String idWithName) {
        super("Not found " + name + " with "+ idWithName);
    }
}
