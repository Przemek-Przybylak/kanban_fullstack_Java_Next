package com.example.kanban.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank (message = "Username is required")
        String username,
        @NotBlank (message = "Password cannot be empty")
        String password) {
}
