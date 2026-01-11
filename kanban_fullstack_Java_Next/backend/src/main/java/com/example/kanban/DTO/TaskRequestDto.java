package com.example.kanban.DTO;

import com.example.kanban.validation.OnCreate;
import com.example.kanban.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record TaskRequestDto(

        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Title is required")
        String title,
        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Description is required")
        String description,
        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Status is required")
        LocalDateTime dueDate,
        String status,
        String user,
        String approvedBy) {
}
