package com.example.kanban.exception;

public class IllegalRoleChangeException extends RuntimeException {
    public IllegalRoleChangeException(String message) {
        super(message);
    }
}