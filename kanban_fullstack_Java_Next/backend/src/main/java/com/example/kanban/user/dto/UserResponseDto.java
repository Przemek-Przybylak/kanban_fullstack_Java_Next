package com.example.kanban.user.dto;

import com.example.kanban.user.model.Role;

public record UserResponseDto(String id, Role role, String username) {
}
