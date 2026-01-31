package com.example.kanban.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String fieldName) {
        super(capitalize(fieldName) + " is already in use");
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
