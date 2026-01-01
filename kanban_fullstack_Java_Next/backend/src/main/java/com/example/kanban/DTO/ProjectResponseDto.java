package com.example.kanban.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponseDto(String id, String title, String description,
                                 LocalDateTime createdAt, LocalDateTime updatedAt, List<shortTasksDto> tasks, List<String> userId) {
}
