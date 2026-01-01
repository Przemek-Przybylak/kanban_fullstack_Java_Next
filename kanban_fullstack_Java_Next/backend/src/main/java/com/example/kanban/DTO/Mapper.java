package com.example.kanban.DTO;

import com.example.kanban.model.Project;
import com.example.kanban.model.Task;
import com.example.kanban.user.model.User;

import java.util.List;

public class Mapper {

    public static ProjectResponseDto toDto(Project entity) {

        List<String> userId = entity.getUsers().stream()
                .map(User::getUsername)
                .toList();

        return new ProjectResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getTasks().stream()
                        .map(task -> new shortTasksDto(task.getId(), task.getTitle()))
                        .toList(),
                userId
        );
    }

    public static Project fromDto(ProjectRequestDto dto) {
        Project project = new Project();

        project.setTitle(dto.title());
        project.setDescription(dto.description());

        return project;
    }

    public static Project fromDto(ProjectPatchRequestDto dto) {
        Project project = new Project();

        project.setTitle(dto.title());
        project.setDescription(dto.description());

        return project;
    }

    public static TaskResponseDto toDto(Task entity) {
        return new TaskResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getApprovedBy(),
                entity.getDueDate(),
                entity.getCreatedAt(),
                new shortProjectDto(entity.getProject().getId(), entity.getProject().getTitle())
        );
    }

    public static Task fromDto(TaskRequestDto taskDto) {
        Task task = new Task();

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(taskDto.status());
        task.setDueDate(taskDto.dueDate());
        task.setApprovedBy(taskDto.approvedBy());

        return task;
    }

    public static Task fromDto(TaskPatchRequestDto taskDto) {
        Task task = new Task();

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(taskDto.status());
        task.setDueDate(taskDto.dueDate());
        task.setApprovedBy(taskDto.approvedBy());

        return task;
    }
}
