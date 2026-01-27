package com.example.kanban.user.model;

import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequest(
        @NotNull(message = "Role cannot be empty")
        Role role
) {}
