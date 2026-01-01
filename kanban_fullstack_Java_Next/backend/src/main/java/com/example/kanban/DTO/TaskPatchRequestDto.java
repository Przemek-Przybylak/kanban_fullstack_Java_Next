package com.example.kanban.DTO;

import java.time.LocalDateTime;

public record TaskPatchRequestDto(
        String title,
        String description,
        String status,
        String approvedBy,
        LocalDateTime dueDate
) {
}