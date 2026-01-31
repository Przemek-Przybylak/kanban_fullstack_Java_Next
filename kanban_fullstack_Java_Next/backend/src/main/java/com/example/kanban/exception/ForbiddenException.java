package com.example.kanban.exception;

import org.springframework.web.client.HttpClientErrorException;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String name) {
        super("You don't have permission for this " + name);
    }
}
