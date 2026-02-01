package com.example.kanban.DTO;

import jakarta.validation.constraints.NotBlank;

public record TaskStatusRequest(@NotBlank String status) {}
