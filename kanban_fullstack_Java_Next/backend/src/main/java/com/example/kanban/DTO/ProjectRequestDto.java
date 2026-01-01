package com.example.kanban.DTO;

import com.example.kanban.validation.OnCreate;
import com.example.kanban.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDto(
        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Title is required")
        String title,
        String description) {
}
