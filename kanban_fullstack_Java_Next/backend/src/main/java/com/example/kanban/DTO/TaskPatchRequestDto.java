package com.example.kanban.DTO;

import java.time.LocalDate;

public record TaskPatchRequestDto(
        String title,
        String description,
        String status,
        String approvedBy,
        LocalDate dueDate
) {
}