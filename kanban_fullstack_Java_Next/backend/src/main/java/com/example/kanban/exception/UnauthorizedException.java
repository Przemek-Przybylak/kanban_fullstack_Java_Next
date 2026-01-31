package com.example.kanban.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String name) {
        super("Invalid " + name);
    }
}
