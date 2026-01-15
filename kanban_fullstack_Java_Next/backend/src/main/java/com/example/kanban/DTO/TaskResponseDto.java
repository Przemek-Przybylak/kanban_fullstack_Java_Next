package com.example.kanban.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponseDto(String id, String title, String description, String status,
                              String approvedBy, LocalDate dueDate, LocalDateTime createdAt,
                              shortProjectDto project, String username) {
}
