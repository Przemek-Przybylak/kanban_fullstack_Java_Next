package com.example.kanban.DTO;

import java.time.LocalDateTime;

public record TaskResponseDto(String id, String title, String description, String status,
                              String approvedBy, LocalDateTime dueDate, LocalDateTime createdAt,
                              shortProjectDto project) {
}
